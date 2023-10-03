// Função para carregar produtos
function carregarProdutos() {
    // GET para endpoint da API
    fetch('https://650a2b30f6553137159c79ee.mockapi.io/produtos')
        // Prometo que deve vir uma resposta da api, se vir mesmo converta essa resposta em .json
        .then(response => response.json())
        // Se vir a resposta e ela foi convertida, crie o seguinte HTML
        .then(data => {
            // Local onde o HTML vai ser inserido, selecione o elemento que tem o ID lista-de-ofertas, que é a section onde os produtos estão
            // selecione também o elemento com a classe "row", que a classe do bootstrap para definir uma linha horizontal em layout de grade
            let row = document.querySelector('#lista-de-ofertas .row');
            data.forEach(produto => {

                // cria um elemento <div/>
                let col = document.createElement('div');
                // atribui a clase 'col mb-3
                col.className = 'col mb-3';

                // cria um elemento <div/>
                let card = document.createElement('div');
                // atribui a clase 'card'
                card.className = 'card';
                // define um id 
                //card.dataset.id = produto.id;

                // cria um elemento <img/>
                let img = document.createElement('img');
                // define o atributo src, esse .img é como ta na API
                img.src = produto.img;
                // atribui a clase 'card-img-top'
                img.className = 'card-img-top';

                // cria um elemento <div/>
                let cardBody = document.createElement('div');
                // atribui a clase 'card-body'
                cardBody.className = 'card-body';

                // cria um elemento <div/>
                let productTitleDiv = document.createElement('div');
                // atribui a clase 'product-title'
                productTitleDiv.className = 'product-title';

                // cria um elemento <h5/>
                let cardTitle = document.createElement('h5');
                // atribui a clase 'card-title'
                cardTitle.className = 'card-title';
                // define o texto para o h5, pegando o 'name' da API
                cardTitle.textContent = produto.name;

                // Anexa o elemento <h5> ao elemento <div> com a classe ‘product-title’.
                // A ordem o appendChild influencia o modo que a arvore DOM vai ser criada. Nesse caso vamos precisar "organizar" primeiro o cartão no DOM, para então inserir as informações nele
                productTitleDiv.appendChild(cardTitle);

                let productDescriptionDiv = document.createElement('div');
                productDescriptionDiv.className = 'product-description';

                let cardText = document.createElement('p');
                cardText.className = 'card-text';

                // Anexa o elemento <p> ao elemento <div> com a classe ‘product-description’.
                // Organizando a div "cardText" no cartão "productDescriptionDiv"
                productDescriptionDiv.appendChild(cardText);

                let productActionDiv = document.createElement('div');
                productActionDiv.className = 'product-action';

                let btn = document.createElement('a');
                btn.onclick = function () { adicionarItem(produto); };
                btn.className = 'btn btn-outline-dark';
                btn.textContent = 'Comparar';

                // Anexa o elemento <a> ao elemento <div> com a classe ‘product-action’.
                productActionDiv.appendChild(btn);

                // Na arvore DOM o 'cardBody' é o pai e productTitleDiv, productDescriptionDiv e productActionDiv seus filhos
                cardBody.appendChild(productTitleDiv);
                cardBody.appendChild(productDescriptionDiv);
                cardBody.appendChild(productActionDiv);

                // No DOM, card é o pai e img e cardBody são filhos dele
                card.appendChild(img);
                card.appendChild(cardBody);

                col.appendChild(card);

                row.appendChild(col);
            });
        })
        .catch(error => console.error('Erro:', error));
}

// Chamar a função quando a página carregar
window.onload = carregarProdutos;
