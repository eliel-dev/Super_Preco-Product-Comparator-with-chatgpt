// Função para carregar produtos
function carregarProdutos() {
    fetch('https://650a2b30f6553137159c79ee.mockapi.io/produtos')
        .then(response => response.json())
        .then(data => {
            let row = document.querySelector('#lista-de-ofertas .row');
            data.forEach(produto => {
                row.innerHTML += `
                    <div class="col mb-3">
                        <div class="card">
                            <img src="${produto.img}" class="card-img-top">
                            <div class="card-body">
                                <div class="product-title">
                                    <h5 class="card-title">${produto.name}</h5>
                                </div>
                                <div class="product-description">
                                </div>
                                <div class="product-action">
                                    <a onclick="" class="btn btn-outline-dark" href="detalhes.html?id=${produto.id}&name=${produto.name}"">Comparar</a>
                                </div>
                            </div>
                        </div>
                    </div>`;
            });
        }) 
        .catch(error => console.error('Erro:', error));
}

// Chamar a função quando a página carregar
window.onload = carregarProdutos;
