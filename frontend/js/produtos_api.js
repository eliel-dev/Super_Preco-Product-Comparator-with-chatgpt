// Função para carregar produtos
function carregarProdutos(pageNumber) {
    fetch(`http://localhost:8080/produto/produtos/?page=${pageNumber}`)
        .then(response => response.json())
        .then(data => {
            let row = document.querySelector('#lista-de-ofertas .row');
            // lista com grupos que já foram adicionados, serve para colocar 1 produto por grupo na pagina inicial
            let gruposAdicionados = [];
            data.forEach(produto => {
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
            });
        })
        .catch(error => console.error('Erro:', error));
}


document.querySelectorAll('.page-link').forEach((link, index) => {
    link.addEventListener('click', (event) => {
        event.preventDefault();
        carregarProdutos(index + 1);
    });
});



document.querySelector('input[type="search"]').addEventListener('input', function(event) {
    // Obtém o termo de pesquisa do campo de entrada
    let searchTerm = event.target.value;

    // Chama a função de autocomplete
    autocomplete(searchTerm);
});

function autocomplete(searchTerm) {
    fetch(`http://localhost:8080/produto/autocomplete/?searchTerm=${searchTerm}`)
        .then(response => response.json())
        .then(data => {
            let listaSugestoes = document.querySelector('#lista-sugestoes');
            listaSugestoes.innerHTML = '';

            data.slice(0, 5).forEach(produto => {
                let itemLista = document.createElement('li');
                itemLista.textContent = produto.nome;
                itemLista.addEventListener('click', function() {
                    document.querySelector('input[type="search"]').value = produto.nome;
                    setTimeout(function() {
                        listaSugestoes.innerHTML = '';  // Limpar a lista de sugestões quando um item é clicado
                    }, 100);
                });
                listaSugestoes.appendChild(itemLista);
            });
        })
        .catch(error => console.error('Erro:', error));
}

function carregarProdutosPorPesquisa(searchTerm) {
    if(!searchTerm.trim()) {
        return; // Retorna sem fazer nada se o campo de pesquisa estiver vazio
    }
    fetch(`http://localhost:8080/produto/autocomplete/?searchTerm=${searchTerm}`)
        .then(response => response.json())
        .then(data => {
            let row = document.querySelector('#lista-de-ofertas .row');
            row.innerHTML = '';
            data.forEach(produto => {
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
            });
        })
        .catch(error => console.error('Erro:', error));
}

let inputSearch = document.querySelector('input[type="search"]');
let listaSugestoes = document.querySelector('#lista-sugestoes');

inputSearch.addEventListener('focus', function() {
    listaSugestoes.style.display = "block"; // Mostra sugestões quando o campo de pesquisa recebe foco
});

inputSearch.addEventListener('blur', function() {
    setTimeout(function() {
        listaSugestoes.style.display = "none"; // Esconde as sugestões quando o campo perde o foco (após um delay curto)
    }, 150);
});

// Chamar a função quando a página carregar
window.onload = carregarProdutos;

