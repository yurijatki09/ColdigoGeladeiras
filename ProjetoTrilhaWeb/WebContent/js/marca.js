/**
 * 
 */
COLDIGO.marca = new Object();

$(document).ready(function(){
	
	//Cadastra uma nova marca no BD
	COLDIGO.marca.cadastrar = function(){
		
		var marca = new Object();
		marca.nome = document.frmAddMarca.nome.value;
		
		if(marca.nome==""){
			COLDIGO.exibirAviso("Preencha o nome!");
		}else{
			
			$.ajax({
				
				type:"POST",
				url: COLDIGO.PATH + "marca/inserir",
				data:JSON.stringify(marca),
				success: function(msg){
					COLDIGO.exibirAviso(msg);
					$("#addMarca").trigger("reset");
					COLDIGO.marca.buscar();
				},
				error: function(info){
					COLDIGO.exibirAviso(info.responseText);
				COLDIGO.marca.buscar();
				}
			});
		}
	};
	
	//Busca no BD e exibe na página as marcas que atendem à solicitação do usuário
	COLDIGO.marca.buscar = function(){
		
		var valorBusca = $("#campoBuscaMarca").val();
		
		$.ajax({
			type: "GET",
			url: COLDIGO.PATH + "marca/buscarNome",
			data: "valorBusca="+valorBusca,
			success: function(dados){
				
				dados = JSON.parse(dados);
				
				//console.log(dados);
				$("#listaMarca").html(COLDIGO.marca.exibir(dados));
				
			},
			error: function(info){
				COLDIGO.exibirAviso(info.responseText);
			}
		});
	};
	
	//Transforma os dados das marcas recebidas do servidor em uma tabela HTML
	COLDIGO.marca.exibir = function(listaDeMarcas){
		var status = "";
		var tabela = "<table>" +
		"<tr>" +
		"<th>Nome</th>" +
		"<th class='acoes'>Ações</th>" +
		"<th>On/Off</th>" +
		"</tr>";
		
		if(listaDeMarcas != undefined && listaDeMarcas.length > 0){
			
			for (var i=0;i<listaDeMarcas.length; i++){
				if(listaDeMarcas[i].status == 1){
					status = "checked";
				}else{
					status = "";
				}
				tabela += "<tr>" +
				           "<td>"+listaDeMarcas[i].nome+"</td>" +
					       "<td>" +
								"<a onclick=\"COLDIGO.marca.exibirEdicao('"+listaDeMarcas[i].id+"')\"><img src='../../imgs/edit.png' alt='Editar registro'></a>" +
								"<a onclick=\"COLDIGO.marca.excluir('"+listaDeMarcas[i].id+"')\"><img src='../../imgs/delete.png' alt='Excluir registro'></a>" +
						   "</td>" +
						   "<td>" +
								"<label class='switch'>" +
									"<input onclick=\"COLDIGO.marca.editaStatus('"+listaDeMarcas[i].id+"')\" type='checkbox' "+ status +">" +
									"<span class='slider round'></span>" +
								"</label>" +
							"</td>" +
						  "</tr>";
			}
			
		}else if(listaDeMarcas == ""){
			tabela += "<tr><td colspan='2'>Nenhum registro encontrado</td></tr>";
		}
		tabela += "</table>";
		
		return tabela;
	};
	
	//Execute a função de busca ao carregar a página
	COLDIGO.marca.buscar();
	
	//Excluir a marca selecionada
	COLDIGO.marca.excluir = function(id){
		$.ajax({
			type:"DELETE",
			url: COLDIGO.PATH + "marca/excluir/"+id,
			success: function(msg){
				COLDIGO.exibirAviso(msg);
				COLDIGO.marca.buscar();
			},
			error: function(info){
				COLDIGO.exibirAviso(info.responseText);
				COLDIGO.marca.buscar();
			}
		});
	};
	
	//Carrega no BD os dados da marca selecionada para alteração e coloca-as no formulário de alteração
	COLDIGO.marca.exibirEdicao = function(id){
		$.ajax({
			type:"GET",
			url: COLDIGO.PATH + "marca/buscarPorId",
			data: "id="+id,
			success: function(marca){
				
				document.frmEditaMarca.idMarca.value = marca.id;
				document.frmEditaMarca.nome.value = marca.nome;
				
				var modalEditaMarca = {
					title: "Editar Marca",
					height: 200,
					width: 500,
					modal: true,
					buttons:{
						"Salvar": function(){
							COLDIGO.marca.editar();
							
						},
						"Cancelar": function(){
							$(this).dialog("close");
						}
					},
					close: function(){
						//caso o usuário simplesmente feche a caixa de edição não deve acontecer nada
					}
				};
				
				$("#modalEditaMarca").dialog(modalEditaMarca);
			},
			error: function(info){
				COLDIGO.exibirAviso(info.responseText);
				COLDIGO.marca.buscar();
			}
		});
	};
	
	//Realiza edições dos dados no BD
	COLDIGO.marca.editar = function(){
		
		var marca = new Object();
		marca.id = document.frmEditaMarca.idMarca.value;
		marca.nome = document.frmEditaMarca.nome.value;
		
		$.ajax({
			type:"PUT",
			url: COLDIGO.PATH + "marca/alterar",
			data:JSON.stringify(marca),
			success: function(msg){
				COLDIGO.exibirAviso(msg);
				COLDIGO.marca.buscar();
				$("#modalEditaMarca").dialog("close");
			},
			error: function(info){
				COLDIGO.exibirAviso(info.responseText);
				COLDIGO.marca.buscar();
			}
		});
	};
	
	COLDIGO.marca.editaStatus = function(id){
		
		$.ajax({
			type:"PUT",
			url: COLDIGO.PATH + "marca/alterarStatus/"+id,
			success: function(msg){
				COLDIGO.exibirAviso(msg);
				COLDIGO.marca.buscar();
			},
			error: function(info){
				//console.log(info);
				COLDIGO.exibirAviso(info.responseText);
				COLDIGO.marca.buscar();
			}
		});
		
	};
});