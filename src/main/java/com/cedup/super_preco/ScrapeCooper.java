package com.cedup.super_preco;

import com.cedup.super_preco.model.ProdutoDTO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ScrapeCooper {

    public List<ProdutoDTO> scrapeProducts () {
        List<ProdutoDTO> produtos = new ArrayList<>();
        try {
            // para contar o total de produtos que vão ser raspados
            int totalProductCount = 0;

            // URL base do site
            String baseUrl = "https://www.minhacooper.com.br/loja/centro-timbo/produto/listar/";
            // Lista de IDs das categorias que foram tiradas da url do site
            List<String> categoryIds = Arrays.asList("12", "644", "166", "199", "97", "7");

            // Iterando sobre cada ID de categoria
            for (String categoryId : categoryIds) {
                // começa na página em 1
                int pageNumber = 1;
                // para contar o total de produtos que vão ser raspados por categoria/link
                int productCount = 0;
                // variável inicia como verdadeira, indicando que existe uma próxima página, ou seja, que existe um elemento com a classe 'ajax-pagination'
                boolean hasNextPage = true;

                // loop para fazer a rapagem, onde interrompe quando um elemento com a classe 'ajax-pagination' não for encontrado mais
                while (hasNextPage) {
                    // Construindo a URL da categoria atual, concatenando url base + id da categoria da lista 'idsCategorias' + parte da url que está relacionado com a pagina atual (?page=) + numero da pagina gerado pelo contador
                    String url = baseUrl + categoryId + "?page=" + pageNumber;

                    // Conectando à página atual
                    Connection connection = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");

                    // cookie necessário para a pagina da cooper funcionar
                    connection.cookie("subsidiaryId", "centro-timbo");

                    Document doc = connection.get();

                    // Selecione os elementos com a classe .product-variation__details, que é a div pai dos elementos product-variation__...
                    Elements products = doc.select(".product-variation");

                    // Iterando sobre cada produto = para cada elemento achado com a classe 'product-variation__details' faça isso:
                    for (Element product : products) {
                        // atribui para elementoNomeProduto o primeiro elemento achado com a classe "product-variation__name"
                        Element productNameElement = product.selectFirst(".product-variation__name");
                        //
                        String productName = productNameElement.text();
                        String productHref = "https://www.minhacooper.com.br" + productNameElement.attr("href");
                        // Alguns produtos podem ter um desconto = html com preço é diferente
                        Element productFinalPriceElement = product.selectFirst(".product-variation__final-price");
                        if (productFinalPriceElement == null) {
                            // Se o produto tem um preço com desconto, selecione o elemento com a classe '.preco-desconto'
                            productFinalPriceElement = product.selectFirst(".preco-desconto");
                        }
                        String productFinalPrice = productFinalPriceElement.text();
                        // Remover todos os caracteres não numéricos
                        productFinalPrice = productFinalPrice.replaceAll("[^\\d.,]", "");
                        // Substituir vírgulas por pontos
                        productFinalPrice = productFinalPrice.replace(',', '.');
                        // Converter para double
                        double priceDouble = Double.parseDouble(productFinalPrice);
                        String productImageLink =  "https:" + product.parent().selectFirst(".product-variation__image-container img").attr("src");

                        // cria uma nova instância de ProdutoDTO
                        ProdutoDTO productInfo = new ProdutoDTO(0, 1, 1, productName, priceDouble, productHref, productImageLink);
                        // adiciona o produto à lista
                        produtos.add(productInfo);

                        System.out.println("Nome do produto: " + productName);
                        System.out.println("Href do produto: " + productHref);
                        System.out.println("Preço final do produto: " + productFinalPrice);
                        System.out.println("Link da imagem do produto: " + productImageLink);
                        System.out.println("---------------------------------------------------------------------------------------------------------------------");
                        productCount++;
                        totalProductCount++;
                    }

                    // usando jsoup seleciona todos elementos com a classe ajax-pagination e armazena em 'nextPageElement'
                    Elements nextPageElement = doc.select(".ajax-pagination");
                    // operador lógico 'NOT':
                    // quando 'nextPageElement' for true (lista nextPageElement está vazia) '!' inverte e hasNextPage recebe 'false', indicando que NÂO tem uma próxima página
                    // quando 'nextPageElement' for false (lista nextPageElement não está vazia) '!' inverte e hasNextPage recebe 'true', indicando que TEM uma próxima página
                    hasNextPage = !nextPageElement.isEmpty();

                    // Incrementando o número da página para a próxima iteração
                    pageNumber++;
                }
                System.out.println("Número de produtos raspados para a categoria " + categoryId + ": " + productCount);
                System.out.println("###############################################################################################################################");
            }
            System.out.println("Total de produtos raspados: " + totalProductCount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return produtos;
    }
}
