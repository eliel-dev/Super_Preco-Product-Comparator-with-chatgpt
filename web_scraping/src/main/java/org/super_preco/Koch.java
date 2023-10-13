package org.super_preco;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;

public class Koch {
    public static void main(String[] args) {
        try {
            // para contar o total de produtos que vão ser raspados
            int totalProductCount = 0;

            // URL base do site
            String baseUrl = "https://www.superkoch.com.br";
            // Lista de URLs usando o método 'asList'
            List<String> categoryPaths = Arrays.asList(
                    "/mercearia/cereal-farinaceo/arroz",
                    "/mercearia/azeite-oleo/azeite-oleo",
                    "/mercearia/cereal-farinaceo/feijao",
                    "/mercearia/massas",
                    "/mercearia/confeitaria/acucar",
                    "/mercearia/molhos-condimentos/sal"
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

                    // Selecione os elementos com a classe .product-item-info, que é a div pai dos elementos product-item-...
                    Elements products = doc.select(".product-item-info");

                    // Itere sobre cada produto
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

                        System.out.println("Nome do produto: " + productName);
                        System.out.println("Href do produto: " + productHref);
                        System.out.println("Preço final do produto: " + productPrice);
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
    }
}
