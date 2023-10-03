/*!
* Start Bootstrap - Shop Item v5.0.6 (https://startbootstrap.com/template/shop-item)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-shop-item/blob/master/LICENSE)
*/
// This file is intentionally blank
// Use this file to add JavaScript to your project

function incrementar(elemento) {
    console.log('Função incrementar chamada');
    var input = elemento.parentNode.querySelector('input[type=number]');
    input.stepUp();
    atualizarPreco(input);

}

function decrementar(elemento) {
    console.log('Função decrementar chamada');
    var input = elemento.parentNode.querySelector('input[type=number]');
    if (input.value > 0) {
        input.stepDown();
        atualizarPreco(input);
    }
}

// Função para remover um item
function removerItem(linha) {
    linha.remove();
}

function atualizarPreco(input) {
    console.log('Função atualizarPreco chamada');
    var quantidade = Math.max(1, input.value); // Garante que a quantidade nunca seja menor que zero
    input.value = quantidade; // Atualiza o valor do campo de entrada para refletir a quantidade corrigida
    var precos = input.parentNode.parentNode.parentNode.querySelectorAll('.preco');

    precos.forEach(function (preco) {
        var precoBase = parseFloat(preco.dataset.precoBase.replace(',', '.'));
        var novoPreco = (precoBase * quantidade).toFixed(2);
        preco.textContent = 'R$ ' + novoPreco.replace('.', ',');
    });
}

// Função para adicionar um item
function adicionarItem(element) { // "element" representa o elemento HTML que é passado como argumento na camada da função, nesse caso o próprio botão
    // Considerando a arvore DOM, como as informações que eu quero estão a cima do botão "comparar", vai ser necessário subir a arvore DOM a partir do "element"

    //-----------------------------------------------------------------------------------
    // subir a árvore DOM:
    // parentElement e parentNode: Usados para obter o elemento pai de um elemento atual.
    //-------------------
    // descer a árvore DOM:
    // children: Usado para obter todos os elementos filhos de um elemento atual.
    // childNodes: Semelhante ao children, mas também inclui nós de texto e comentários.
    //-----------------------------------------------------------------------------------

    // element = representa o botão "comparar"
    // element.parentElement =  representa o seu pai do botão "comparar" = <div class="card-body"> </div>
    // element.parentElement.parentElement.dataset é o pai da div (<div class="card-body"> </div>) e presenta a div (<div class="card" data-id="4" data-name="Vodka Destilada Absolut Garrafa 750ml" data-price="20"></div>)

    let produto = element.parentElement.parentElement.dataset;
    console.log(element);
    console.log(element.parentElement);
    console.log(element.parentElement.parentElement.dataset);
    // Obter a lista de itens do LocalStorage
    let itens = JSON.parse(localStorage.getItem('itens'));

    // Se não houver nenhum item ainda, definir a lista de itens como um objeto vazio
    if (itens === null) {
        itens = {};
    }
    // Adicionar o item à lista
    if (!(produto.name in itens)) {
        itens[produto.name] = 1;
    } else {
        itens[produto.name]++;
    }

    // Salvar a nova lista de itens no LocalStorage
    localStorage.setItem('itens', JSON.stringify(itens));

    // Atualizar o contador
    atualizarContador();
}

// Função para atualizar o contador
function atualizarContador() {
    // Obter a lista de itens do LocalStorage
    let itens = JSON.parse(localStorage.getItem('itens'));
    let numeroDeItens;

    // Se não houver nenhum item ainda, definir o número de itens como 0
    if (itens === null) {
        numeroDeItens = 0;
    } else {
        numeroDeItens = Object.keys(itens).length;
    }

    // Atualizar o texto do contador
    document.querySelector('.badge').textContent = numeroDeItens;
}

// Função para zerar o contador
function zerarContador() {
    // Limpar o LocalStorage
    localStorage.clear();

    // Atualizar o contador
    atualizarContador();
}

// Atualizar o contador quando a página é carregada
atualizarContador();


