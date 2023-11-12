// Função para carregar produtos
function carregarProdutos() {
    fetch('http://localhost:8080/produto/')
        .then(response => response.json())
        .then(data => {
            let row = document.querySelector('#lista-de-ofertas .row');
            // lista com grupos que já foram adicionados, serve para colocar 1 produto por grupo na pagina inicial
            let gruposAdicionados = [];
            data.forEach(produto => {
                // verifiqua se o algum produto com o mesmo id_grupo ja está na lista de adicionados, caso não tiver, então coloque um card do novo produto
                if (!gruposAdicionados.includes(produto.id_produto)) {
                    gruposAdicionados.push(produto.id_produto);
                    // criando um novo card e colocando no botão 'comparar' que eu quero passar por parâmetro
                    row.innerHTML += `
                    <div class="col mb-3">
                        <div class="card">
                            <img src="${produto.link_img}" class="card-img-top">
                            <div class="card-body">
                                <div class="product-title">
                                    <h5 class="card-title">${produto.nome}</h5>
                                </div>
                                <div class="product-description">
                                </div>
                                <div class="product-action">
                                <a class="btn btn-outline-dark" href="detalhes.html?id_produto=${produto.id_produto_mercado}&id_mercado=${produto.id_mercado}&nome=${produto.nome}&id_grupo=${produto.id_produto}">Comparar</a>
                                </div>
                            </div>
                        </div>
                    </div>`;
                }
            });
        })
        .catch(error => console.error('Erro:', error));
}

document.querySelector('input[type="search"]').addEventListener('input', function(event) {
    // Obtém o termo de pesquisa do campo de entrada
    let searchTerm = event.target.value;

    // Chama a função de pesquisa
    pesquisar(searchTerm);
});

function pesquisar(searchTerm) {
    fetch(`http://localhost:8080/produto/autocomplete/?searchTerm=${searchTerm}`)
        .then(response => response.json())
        .then(data => {
            // Aqui você pode manipular os dados retornados pela pesquisa
            // Por exemplo, você pode querer exibir as sugestões de pesquisa em uma lista
            let listaSugestoes = document.querySelector('#lista-sugestoes');
            listaSugestoes.innerHTML = '';
            data.forEach(produto => {
                let itemLista = document.createElement('li');
                itemLista.textContent = produto.nome;
                itemLista.addEventListener('click', function() {
                    document.querySelector('input[type="search"]').value = produto.nome;
                    // Esvazia a lista de sugestões
                    listaSugestoes.innerHTML = '';
                });
                listaSugestoes.appendChild(itemLista);
            });
        })
        .catch(error => console.error('Erro:', error));
}

// Adiciona um ouvinte de eventos ao documento
document.addEventListener('click', function(event) {
    let campoPesquisa = document.querySelector('input[type="search"]');
    let listaSugestoes = document.querySelector('#lista-sugestoes');

    // Verifica se o clique ocorreu fora do campo de pesquisa e da lista de sugestões
    if (!campoPesquisa.contains(event.target) && !listaSugestoes.contains(event.target)) {
        // Esvazia a lista de sugestões
        listaSugestoes.innerHTML = '';
    }
});

// Chamar a função quando a página carregar
window.onload = carregarProdutos;

