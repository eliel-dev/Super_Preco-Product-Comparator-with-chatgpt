package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;

public class Cooper {
    public static void main(String[] args) {
        try {
            int totalProdutosRaspados = 0;
            // Lista de IDs das categorias
            List<String> idsCategorias = Arrays.asList("12", "644", "166", "199", "97", "7");
            // URL base do site
            String urlBase = "https://www.minhacooper.com.br/loja/centro-timbo/produto/listar/";

            // Iterando sobre cada ID de categoria
            for (String idCategoria : idsCategorias) {
                // Inicializando a página em 1
                int numPagina = 1;
                int contadorProduto = 0;

                boolean temProximaPagina = true;
                while (temProximaPagina) {
                    // Construindo a URL da categoria atual
                    String url = urlBase + idCategoria + "?page=" + numPagina;

                    // Conectando à página atual
                    Connection conexao = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");

                    // Adicionando o cookie
                    conexao.cookie("subsidiaryId", "centro-timbo");

                    Document doc = conexao.get();

                    // Selecione os elementos com a classe .product-variation__details, que é a div pai dos elementos product-variation__...
                    Elements produtos = doc.select(".product-variation__details");

                    // Iterando sobre cada produto
                    for (Element produto : produtos) {
                        Element elementoNomeProduto = produto.selectFirst(".product-variation__name");
                        String nomeProduto = elementoNomeProduto.text();
                        String hrefProduto = "https://www.minhacooper.com.br" + elementoNomeProduto.attr("href");
                        String precoFinalProduto = produto.selectFirst(".product-variation__final-price").text();
                        String linkImagemProduto =  "https:" + produto.parent().selectFirst(".product-variation__image-container img").attr("src");

                        System.out.println("Nome do produto: " + nomeProduto);
                        System.out.println("Href do produto: " + hrefProduto);
                        System.out.println("Preço final do produto: " + precoFinalProduto);
                        System.out.println("Link da imagem do produto: " + linkImagemProduto);
                        System.out.println("---------------------------------------------------------------------------------------------------------------------");
                        contadorProduto++;
                        totalProdutosRaspados++;
                    }

                    // Verificando se há uma próxima página
                    Elements elementoProximaPagina = doc.select(".ajax-pagination");
                    temProximaPagina = !elementoProximaPagina.isEmpty();

                    // Incrementando o número da página para a próxima iteração
                    numPagina++;
                }
                System.out.println("Número de produtos raspados para a categoria " + idCategoria + ": " + contadorProduto);
                System.out.println("###############################################################################################################################");
            }
            System.out.println("Total de produtos raspados: " + totalProdutosRaspados);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
