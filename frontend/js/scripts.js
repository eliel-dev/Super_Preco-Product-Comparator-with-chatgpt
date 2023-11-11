// chamo a função getParam e armazeno em id_produto o valor do parâmetro 'id_produto'
let id_produto = getParam("id_grupo");

// se tiver um id_produto faça isso:
if (id_produto) {
    // get + url + id_produto
    fetch("http://localhost:8080/produto/grupo/" + id_produto)
        // se eu tiver uma resposta converta ela em .json
        .then(response => response.json())
        // recebe o conteúdo do json
        .then(data => {
            // inserindo o card de mercados onde o produto está disponível
            // seleciona o '#lista-de-ofertas .row'
            let row = document.querySelector('#lista-de-ofertas .row');
            // para cada produto 
            data.forEach(produto => {
                // como vou usar a mesma variável para os 2 mercados, declaro ela antes
                let mercadoLogo, mercadoNome;
                if (produto.id_mercado === 1) {
                    mercadoLogo = "img/cooper-logo.jpg";
                    mercadoNome = "Cooper";
                    // url do cooper é só trocar o id do produto pela palavra 'original' para conseguir imagem em alta qualidade
                    let img_url = produto.link_img;
                    let img_url_parts = img_url.split('/');
                    img_url_parts[img_url_parts.length - 2] = 'original';
                    let new_img_url = img_url_parts.join('/');
                    // defini imagem e texto para elementos com os seguintes ids
                    document.getElementById('imagem').src = new_img_url;
                    document.getElementById('nome_produto').innerText = produto.nome;
                } else if (produto.id_mercado === 2) {
                    mercadoLogo = "img/koch2.webp";
                    mercadoNome = "SuperKoch";
                    if (!document.getElementById('nome_produto').innerText) {
                        document.getElementById('nome_produto').innerText = produto.nome;
                    }
                }
                // cria um div e defini uma classe para el
                let col = document.createElement('div');
                col.className = "col-sm-4 mb-3";

                col.innerHTML = `
                            <div class="card">
                                <div class="card-body d-flex align-items-center">
                                    <img src="${mercadoLogo}" class="card-img-top-detalhes" alt="">
                                    <div>
                                        <h5 class="card-title2">${mercadoNome}</h5>
                                        <p class="card-text">${parseFloat(produto.preco).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}</p>
                                        <a href="${produto.link}" target="_blank" class="btn btn-primary">Ir até o mercado</a>
                                    </div>
                                </div>
                            </div>`;
                row.appendChild(col);
            });
        })
        .catch(error => {
            alert("Ocorreu um erro ao buscar os dados do produto: " + error);
        });
} else {
    window.location.href = "/erro.html";
}
