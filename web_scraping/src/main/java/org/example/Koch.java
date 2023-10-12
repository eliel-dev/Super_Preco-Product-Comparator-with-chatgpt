package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;

public class Koch {
    public static void main(String[] args) {
        try {
            int totalProdutosRaspados = 0;
            // Lista de URLs usando o método 'asList'
            List<String> urls = Arrays.asList(
                    "https://www.superkoch.com.br/mercearia/cereal-farinaceo/arroz",
                    "https://www.superkoch.com.br/mercearia/azeite-oleo/azeite-oleo",
                    "https://www.superkoch.com.br/mercearia/cereal-farinaceo/feijao",
                    "https://www.superkoch.com.br/mercearia/massas",
                    "https://www.superkoch.com.br/mercearia/confeitaria/acucar",
                    "https://www.superkoch.com.br/mercearia/molhos-condimentos/sal"
            );

            // Iterando sobre cada URL
            for (String url : urls) {
                // Inicializando a página em 1
                int numPagina = 1;
                int contadorProduto = 0;

                // Verificando se há uma próxima página
                boolean temProximaPagina = true;
                while (temProximaPagina) {
                    // Conectando à página atual
                    Document doc = Jsoup.connect(url + "?p=" + numPagina)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                            .get();

                    // Selecionando os elementos com a classe .product-item-info
                    Elements produtos = doc.select(".product-item-info");

                    // Iterando sobre cada produto
                    for (Element produto : produtos) {
                        Element elementoProdutoNome = produto.selectFirst(".product-item-link");
                        String productName = elementoProdutoNome.text();
                        String productHref = elementoProdutoNome.attr("href");
                        Elements produtoPrecos = produto.select(".price");
                        String precoProduto;
                        if (produtoPrecos.size() == 2) {
                            precoProduto = produtoPrecos.get(1).text();
                        } else {
                            precoProduto = produtoPrecos.first().text();
                        }

                        System.out.println("Nome do produto: " + productName);
                        System.out.println("Href do produto: " + productHref);
                        System.out.println("Preço final do produto: " + precoProduto);
                        System.out.println("---------------------------------------------------------------------------------------------------------------------");
                        contadorProduto++;
                        totalProdutosRaspados++;
                    }

                    // Verificando se há uma próxima página
                    Elements elementoProximaPagina = doc.select(".pages-item-next");

                    temProximaPagina = !elementoProximaPagina.isEmpty();

                    // Incrementando o número da página para a próxima iteração
                    numPagina++;
                }
                System.out.println("Número de produtos raspados para a categoria " + url + ": " + contadorProduto);
            }
            // Imprimindo o número total de produtos raspados
            System.out.println("Total de produtos raspados: " + totalProdutosRaspados);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
