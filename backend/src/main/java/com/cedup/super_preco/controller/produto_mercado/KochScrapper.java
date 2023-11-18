package com.cedup.super_preco.controller.produto_mercado;

import com.cedup.super_preco.model.mercado.MercadoEntity;
import com.cedup.super_preco.model.produto.ProdutoEntity;
import com.cedup.super_preco.model.produto_mercado.Produto_MercadoEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class KochScrapper {

    public List<Produto_MercadoEntity> scrapeProducts() {
        List<Produto_MercadoEntity> produtos = new ArrayList<>();
        try {
            // para contar o total de produtos que vão ser raspados
            int totalProductCount = 0;

            // URL base do site
            String baseUrl = "https://www.superkoch.com.br";
            // Lista de URLs usando o método 'asList'
            List<String> categoryPaths = Arrays.asList(
                    "/bebidas/refrigerante",
                    "/outros/matinal/cafe"
            );

            // Iterando sobre cada URL
            for (String categoryPath : categoryPaths) {
                // começa na página em 1
                int pageNumber = 1;
                // para contar o total de produtos que vão ser raspados por categoria/link
                int productCount = 0;
                // variável inicia como verdadeira, indicando que existe uma próxima página, ou seja, que existe um elemento com a classe 'pages-item-next'
                boolean hasNextPage = true;

                // loop para fazer a rapagem, onde interrompe quando um elemento com a classe 'pages-item-next' não for encontrado mais
                while (hasNextPage) {

                    // Construindo a URL da categoria atual, concatenando url base + caminho da próxima categoria na lista 'categoryPaths' + parte da url que está relacionado com a pagina atual (?p=) + numero da pagina gerado pelo contador
                    String url = baseUrl + categoryPath + "?p=" + pageNumber;
                    // inicia a conexão com a página a ser raspada,
                    Document doc = Jsoup.connect(url)
                            // cabeçalho do agente
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                            .get();

                    // Seleciona os elementos com a classe .product-item-info, que é a div pai dos elementos product-item-...
                    Elements products = doc.select(".product-item-info");

                    // Itera sobre cada produto
                    for (Element product : products) {
                        Element productNameElement = product.selectFirst(".product-item-link");
                        String productName = productNameElement.text();
                        String productHref = productNameElement.attr("href");
                        // no koch pode ter 2 preços com a mesma classe 'price' para o mesmo produto, porem que importa pra gente é o segundo
                        // então vamos obter todos os elementos com a classe 'price' e armazenar numa variável do tipo Elements
                        Elements productPrices = product.select(".price");
                        // agora vamos declarar uma variável do tipo String que vai receber o preço do produto
                        String productPrice;
                        // Verifica se na lista 'productPrices" tem mais de dois preços (elementos com a classe price)
                        if (productPrices.size() == 2) {
                            // Se tiver, pegue o texto do segundo, na lista get(0) = primeiro // get(1) == segundo
                            productPrice = productPrices.get(1).text();
                        } else {
                            // Se não, pegue o texto do primeiro
                            productPrice = productPrices.first().text();
                        }

                        // formatar o texto do preço para apenas o número
                        // Remover todos os caracteres não numéricos
                        productPrice = productPrice.replaceAll("[^\\d.,]", "");
                        // Substituir vírgulas por pontos
                        productPrice = productPrice.replace(',', '.');
                        // Converter para double
                        double priceDouble = Double.parseDouble(productPrice);

                        // imagem em baixa resolução/otimizada
                        String productImageLink = product.parent().selectFirst(".product-image-photo").attr("src");

                        // cria nova instância de Produto_MercadoDTO
                        Produto_MercadoEntity productInfo = new Produto_MercadoEntity(0, new MercadoEntity(2), new ProdutoEntity("0"), productName, priceDouble, productHref, productImageLink);
                        // adiciona o produto à lista 'produto"
                        produtos.add(productInfo);

                        System.out.println("Nome do produto: " + productName);
                        System.out.println("Href do produto: " + productHref);
                        System.out.println("Preço final do produto: " + productPrice);
                        System.out.println("Link da imagem do produto: " + productImageLink);
                        System.out.println("---------------------------------------------------------------------------------------------------------------------");
                        productCount++;
                        totalProductCount++;
                    }

                    // Verificando se há uma próxima página
                    Elements nextPageElement = doc.select(".pages-item-next");
                    hasNextPage = !nextPageElement.isEmpty();

                    // Incrementando o número da página para a próxima iteração
                    pageNumber++;
                }
                System.out.println("Número de produtos raspados para a categoria " + categoryPath+ ": " + productCount);
            }
            // Imprimindo o número total de produtos raspados
            System.out.println("Total de produtos raspados: " + totalProductCount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return produtos;
    }
}
