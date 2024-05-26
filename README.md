# SUPER PREÇO - COMPARADOR DE PREÇOS DE MERCADOS ONLINE

Super Preço foi desenvolvido para facilitar a comparação de preços de produtos de mercados online. Utilizando técnicas de web scraping e a API GPT da OpenAI, o software salva em um banco de dados produtos de mercados online e depois usa esses dados para criar uma relação de produtos iguais, mesmo quando possuem descrições diferentes.

# Fluxo de Processamento dos Dados
![](image.png)

## Requisitos

- Java Development Kit (JDK) 17+
- Maven 3.5.3+
- MySQL Server: 8.4.0+

## Configurações adcionais

- **Banco de Dados**:
Configure o acesso a um banco de dados MySQL.
As credenciais do banco de dados (URL, nome de usuário, senha) devem ser fornecidas no arquivo "ConnectionSingleton.java".
- **API do ChatGPT**:
Você precisará de uma chave de API válida da OpenAI para usar a funcionalidade de ChatGPT.
Forneça a chave de API no arquivo "application.properties".

## Autores

- [@Eliel Rodrigues](https://github.com/eliel-dev)
- [@Fernanda Paterno](https://github.com/Fernandafp)

## Agradecimentos
Agradecimentos especiais ao orientador [@Roberto Luiz Debarba](https://github.com/RobertoDebarba) e ao [@Centro de Educação Profissional de Timbó (CEDUP)](https://github.com/GitCedup) por todo o suporte e orientação.


## Licença

The codebase is licensed under [GPL v3.0](http://www.gnu.org/licenses/gpl-3.0.html).
