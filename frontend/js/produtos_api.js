// Função para criar um card do produto
function createProductCard(produto) {
    const card = `
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
    return card;
}

// Função para renderizar os produtos
function renderProducts(data) {
    const row = document.querySelector('#lista-de-ofertas .row');
    row.innerHTML = '';

    data.forEach(produto => {
        const card = createProductCard(produto);
        row.innerHTML += card;
    });
}

// Função para carregar produtos
async function carregarProdutos(pageNumber) {
    const url = `http://localhost:8080/produto/produtos/?page=${pageNumber}`;

    const response = await fetch(url);
    const data = await response.json();

    renderProducts(data);

    return data; // Retorne os dados para que eles possam ser usados fora desta função
}

// Função para atualizar os links de página
function updatePageLinks(current) {
    const previousPageElement = document.querySelector('#previous-page');
    const currentPageElement = document.querySelector('#current-page');
    const nextPageElement = document.querySelector('#next-page');

    // Remover a classe 'active' de todos os elementos de página
    document.querySelectorAll('.page-item .page-link').forEach(item => {
        item.classList.remove('active');
    });

    // Adicionar a classe 'active' ao elemento da página atual
    currentPageElement.classList.add('active');

    // Atualizar o href e o número de visualização
    previousPageElement.href = `#page${current - 1}`;
    previousPageElement.textContent = current - 1;

    currentPageElement.href = `#page${current}`;
    currentPageElement.textContent = current;

    nextPageElement.href = `#page${current + 1}`;
    nextPageElement.textContent = current + 1;
}

// Função para carregar produtos com base na pesquisa
async function carregarProdutosPorPesquisa(searchTerm) {
    if (!searchTerm.trim()) {
        return; // Retorna sem fazer nada se o campo de pesquisa estiver vazio
    }

    const url = `http://localhost:8080/produto/autocomplete/?searchTerm=${searchTerm}`;

    const response = await fetch(url);
    const data = await response.json();

    renderProducts(data);
}

// Evento ao carregar a página
window.onload = async () => {
    let pageNum = 1;

    // Lógica para obter o número da página a partir da hash da URL
    if (window.location.hash) {
        const hash = window.location.hash.substring(1);
        const match = hash.match(/^page(\d+)$/);
        if (match) {
            pageNum = Number(match[1]);
        }
    }

    updatePageLinks(pageNum);
    await carregarProdutos(pageNum);

    // Verifique se está na página 1, se sim, esconda o botão e o número da página anterior
    if (pageNum === 1) {
        // Adicionar a classe 'active' ao elemento da página 1
        document.querySelector('#current-page').classList.add('active');
        document.querySelector('#previous').style.display = 'none'; // Esconde o botão "Anterior"
        document.querySelector('#previous-page').style.display = 'none'; // Esconde o número da página anterior
    }
};



// Evento ao clicar no botão "Anterior"
document.querySelector('#previous').addEventListener('click', async (event) => {
    event.preventDefault();
    const currentPageNum = Number(document.querySelector('#current-page').textContent);
    const previousPageNum = currentPageNum - 1;
    if (previousPageNum >= 2) {
        history.pushState(null, '', `#page${previousPageNum}`);
        updatePageLinks(previousPageNum);
        await carregarProdutos(previousPageNum);
        document.querySelector('#next').style.display = 'block';
        document.querySelector('#next-page').style.display = 'block'; // Mostra o número da próxima página
    }
    if (previousPageNum === 1) {
        document.querySelector('#previous').style.display = 'none'; // Esconde o botão "Anterior"
        document.querySelector('#previous-page').style.display = 'none'; // Esconde o número da página anterior
    }
});

// Evento ao clicar no botão "Proximo"
document.querySelector('#next').addEventListener('click', async (event) => {
    event.preventDefault();
    const currentPageNum = Number(document.querySelector('#current-page').textContent);
    const nextPageNum = currentPageNum + 1;

    // Primeiro, checamos se há produtos na próxima página
    const nextProducts = await carregarProdutos(nextPageNum);
    if (nextProducts.length === 10) { // Se temos 10 produtos, não é a última pagina
        // Atualize para a próxima página
        history.pushState(null, '', `#page${nextPageNum}`);
        updatePageLinks(nextPageNum);
        document.querySelector('#next').style.display = 'block';
        document.querySelector('#previous').style.display = 'block';
        document.querySelector('#next-page').style.display = 'block'; // Mostra o número da próxima página
    } else {
        // Se não houver 10 produtos, estamos na última página
        document.querySelector('#next').style.display = 'none';
        document.querySelector('#next-page').style.display = 'none'; // Esconde o número da próxima página
    }
});

// Barra de pesquisa
document.querySelector('input[type="search"]').addEventListener('input', function (event) {
    const searchTerm = event.target.value;
    autocomplete(searchTerm);
});

// Lógica de autocompletar
async function autocomplete(searchTerm) {
    const url = `http://localhost:8080/produto/autocomplete/?searchTerm=${searchTerm}`;

    const response = await fetch(url);
    const data = await response.json();

    const listaSugestoes = document.querySelector('#lista-sugestoes');
    listaSugestoes.innerHTML = '';

    data.slice(0, 5).forEach(produto => {
        const itemLista = document.createElement('li');
        itemLista.textContent = produto.nome;
        itemLista.addEventListener('click', function () {
            document.querySelector('input[type="search"]').value = produto.nome;
            setTimeout(function () {
                listaSugestoes.innerHTML = '';  // Limpar a lista de sugestões quando um item é clicado
            }, 100);
        });
        listaSugestoes.appendChild(itemLista);
    });
}

// Eventos relacionados à barra de pesquisa
const inputSearch = document.querySelector('input[type="search"]');
const listaSugestoes = document.querySelector('#lista-sugestoes');
const sugestoes = listaSugestoes.children;
let indiceactive = -1;

inputSearch.addEventListener('focus', function () {
    listaSugestoes.style.display = "block";
});

inputSearch.addEventListener('blur', function () {
    setTimeout(function () {
        listaSugestoes.style.display = "none";
    }, 150);
});

inputSearch.addEventListener('keydown', function (e) {
    switch (e.key) {
        case 'ArrowDown':
            if (indiceactive >= 0) sugestoes[indiceactive].classList.remove('active');
            if (indiceactive < sugestoes.length - 1) {
                indiceactive++;
                sugestoes[indiceactive].classList.add('active');
                inputSearch.value = sugestoes[indiceactive].textContent;
            }
            break;
        case 'ArrowUp':
            if (indiceactive > 0) {
                sugestoes[indiceactive].classList.remove('active');
                indiceactive--;
                sugestoes[indiceactive].classList.add('active');
                inputSearch.value = sugestoes[indiceactive].textContent;
            }
            break;
    }
});
