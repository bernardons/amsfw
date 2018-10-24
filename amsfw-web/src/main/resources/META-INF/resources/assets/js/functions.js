function validarMesAno(obj) {  
	
	var mes = obj.value.substring(0,2);	
	
	if(mes > 12 || mes == "00"){		
		obj.value = "";
	}	
    return true; 
}

function addClassNegrito(componente){	
	$(componente.parentNode.parentNode).attr("aria-selected", "true");
	$(componente.parentNode.parentNode).addClass("ui-state-highlight");
	$(componente.parentNode.parentNode).removeClass("ui-state-hover");
}

function removeClassNegrito(componente){
	$(componente.parentNode.parentNode).attr("aria-selected", "false");
	$(componente.parentNode.parentNode).removeClass("ui-state-highlight");
}

function addLinhaEditavel(dataTable, inputFocus){
    var tabelaID =  $("tbody[id$='" + dataTable + "_data']").attr('id');    
    var tabela = document.getElementById(tabelaID); 
    var linhas = tabela.getElementsByTagName('tr'); 
    $(linhas[linhas.length - 1]).addClass("ui-row-editing");
    $("input[id$='" + inputFocus + "']").focus();
    $(linhas[linhas.length - 1]).closest('tr').find('input').each(function() {
        this.value = "";
    });
}


function transferirValorInputs(inputDestino, value){
	document.getElementById($("input[id$='" + inputDestino + "']").attr('id')).value = value;
}

function addFocus(dataTable, inputFocus) {
	var tabelaID =  $("tbody[id$='" + dataTable + "_data']").attr('id');    
    var tabela = document.getElementById(tabelaID); 
    var linhas = tabela.getElementsByTagName('tr'); 
    $(linhas[linhas.length - 1]).focus();
    $("input[id$='" + inputFocus + "']").focus();
}

function deixaNumeros(evt){
		evt = (evt) ? evt : window.event;
	    var codigo = (evt.which) ? evt.which : evt.keyCode;
	    // Teclas de usabilidade. Home End Delete Backspace seta direita esquerda tab.
	    if (codigo == 8 || codigo == 9
	    		|| codigo == 35 || codigo == 36 
	    		|| codigo == 37 || codigo == 39) {
	    	return true;
	    }
	   
	    if (codigo > 31 && (codigo < 48 || codigo > 57)) {
	    	return false;
	    }
	    
	    return true;
}

function validaSomenteNumeros(componente){
	if(isNaN(componente.value)){
		componente.value = '';
	}
}

var dateTranslation;

function loadJsCssFile(filename, filetype) {
	if (filetype == "js") { 
		$("head").append($("<script src='" + filename + "' type='text/javascript' />"));
	} else if (filetype == "css") { 
		$("head").append($("<link rel='stylesheet' href='" + filename + "' type='text/css' media='screen' />"));
	}
}

function onlyDigits(evt) {	  
    var charCode = (evt.which) ? evt.which : event.keyCode;    
    
    if ((charCode > 48 && charCode < 57) || charCode == 8)
       return true;  
    return false; 
}

function clickEnter() {
	var panel = $('.btn-click');
	eval(panel.trigger('click'));
}

function focusAbaRequired() {
	if($(".ui-message-error-detail").length > 0) {
		// Pega o primeiro item de erro
		var elemento = $(".ui-message-error-detail" ).first();
		// Pega a aba melhor 
		var divTeste = elemento.closest(".ui-tabs-panel");
		var idDiv = divTeste.attr('id');
		// Pega o link para a aba atual e efetua o click
		var expressaoLinkAtual = 'a[href="#'+ idDiv + '"]';
		var linkAba = $(expressaoLinkAtual);
		linkAba.click();
	}
}	
function mask(campo, formato, conteudo, event) {

	valor = campo.value;

	var i;
	var auxPonto = formato;
	var auxBarra = formato;
	var auxHifen = formato;
	var auxDblPonto = formato;
	var auxAbrePar = formato;
	var auxFechaPar = formato;
	var auxVirgula = formato;
	var tamanho = formato.length;
	var posPonto = new Array(tamanho);
	var posBarra = new Array(tamanho);
	var posHifen = new Array(tamanho);
	var posDblPonto = new Array(tamanho);
	var posAbrePar = new Array(tamanho);
	var posFechaPar = new Array(tamanho);
	var posVirgula = new Array(tamanho);

	campo.maxLength = tamanho;

	if (event.keyCode != 17) {
		switch (conteudo) {
		case 1: // Verifica se soh podem ser entrados valores numericos
			if (!(event.keyCode >= 48 && (event.keyCode <= 57)))
				event.keyCode = 0;
			break;
		case 2: // Somente Letras
			if (!((event.keyCode >= 97 && event.keyCode <= 122) || event.keyCode >= 65
					&& event.keyCode <= 90))
				event.keyCode = 0;
			break;
		case 3: // Letras e numeros
			if (!((event.keyCode >= 48 && event.keyCode <= 57)
					|| (event.keyCode >= 97 && event.keyCode <= 122) || (event.keyCode >= 65 && event.keyCode <= 90)))
				event.keyCode = 0;
			break;
		}
	}

	// ----------------------------- PEGA A FORMATACAO DA MASCARA
	// ------------------
	for (i = 0; i < tamanho; i++) {

		posPonto[i] = auxPonto.indexOf('.');
		posBarra[i] = auxBarra.indexOf('/');
		posHifen[i] = auxHifen.indexOf('-');
		posDblPonto[i] = auxDblPonto.indexOf(':');
		posAbrePar[i] = auxAbrePar.indexOf('(');
		posFechaPar[i] = auxFechaPar.indexOf(')');
		posVirgula[i] = auxVirgula.indexOf(',');

		auxPonto = auxPonto.substring(posPonto[i] + 1, tamanho);
		auxBarra = auxBarra.substring(posBarra[i] + 1, tamanho);
		auxHifen = auxHifen.substring(posHifen[i] + 1, tamanho);
		auxDblPonto = auxDblPonto.substring(posDblPonto[i] + 1, tamanho);
		auxAbrePar = auxAbrePar.substring(posAbrePar[i] + 1, tamanho);
		auxFechaPar = auxFechaPar.substring(posFechaPar[i] + 1, tamanho);
		auxVirgula = auxVirgula.substring(posVirgula[i] + 1, tamanho);

		if (i > 0) {
			posPonto[i] = posPonto[i] + posPonto[i - 1];
			posBarra[i] = posBarra[i] + posBarra[i - 1];
			posHifen[i] = posHifen[i] + posHifen[i - 1];
			posDblPonto[i] = posDblPonto[i] + posDblPonto[i - 1];
			posAbrePar[i] = posAbrePar[i] + posAbrePar[i - 1];
			posFechaPar[i] = posFechaPar[i] + posFechaPar[i - 1];
			posVirgula[i] = posVirgula[i] + posVirgula[i - 1];
			posPonto[i] = posPonto[i] + 1;
			posBarra[i] = posBarra[i] + 1;
			posHifen[i] = posHifen[i] + 1;
			posDblPonto[i] = posDblPonto[i] + 1;
			posAbrePar[i] = posAbrePar[i] + 1;
			posFechaPar[i] = posFechaPar[i] + 1;
			posVirgula[i] = posVirgula[i] + 1;
		}
	}

	// Retirando a máscara
	for (i = 0; i < campo.value.length; i++) {
		valor = valor.replace('-', '');
		valor = valor.replace('(', '');
		valor = valor.replace(')', '');
		valor = valor.replace(':', '');
		valor = valor.replace('/', '');
		valor = valor.replace('.', '');
		valor = valor.replace(',', '');
	}

	// Faz a validação se for apenas número
	// utilizado para fazer a validação de Ctrl+V
	if (conteudo == 1) {
		if (isNaN(valor)) {
			if (isNaN(valor.charAt(valor.length - 2)))
				valor = "";
			else
				valor = valor.substring(0, valor.length - 1);
		}
	}

	indicePonto = 0;
	indiceBarra = 0;
	indiceHifen = 0;
	indiceDblPonto = 0;
	indiceVirgula = 0;
	indiceAbrePar = 0;
	indiceFechaPar = 0;

	// Varre o campo aplicando a máscara
	for (i = 0; i < valor.length; i++) {
		if (i == posPonto[indicePonto]) {
			if (valor.charAt(i) != '.') {
				if (i == 0) {
					valor = '.' + valor;
				} else if (i == valor.length) {
					valor = valor + '.';
				} else {
					valor = valor.substring(0, i) + '.' + valor.substring(i);
				}
				indicePonto++;
			}
		}
		if (i == posBarra[indiceBarra]) {
			if (valor.charAt(i) != '/') {
				if (i == 0) {
					valor = '/' + valor;
				} else if (i == valor.length) {
					valor = valor + '/';
				} else {
					valor = valor.substring(0, i) + '/' + valor.substring(i);
				}
				indiceBarra++;
			}
		}

		if (i == posHifen[indiceHifen]) {
			if (valor.charAt(i) != '-') {
				if (i == 0) {
					valor = '-' + valor;
				} else if (i == valor.length) {
					valor = valor + '-';
				} else {
					valor = valor.substring(0, i) + '-' + valor.substring(i);
				}
				indiceHifen++;
			}
		}

		if (i == posDblPonto[indiceDblPonto]) {
			if (valor.charAt(i) != ':') {
				if (i == 0) {
					valor = ':' + valor;
				} else if (i == valor.length) {
					valor = valor + ':';
				} else {
					valor = valor.substring(0, i) + ':' + valor.substring(i);
				}
				indiceDblPonto++;
			}
		}

		if (i == posAbrePar[indiceAbrePar]) {
			if (valor.charAt(i) != '(') {
				if (i == 0) {
					valor = '(' + valor;
				} else if (i == valor.length) {
					valor = valor + '(';
				} else {
					valor = valor.substring(0, i) + '(' + valor.substring(i);
				}
				indiceAbrePar++;
			}
		}

		if (i == posFechaPar[indiceFechaPar]) {
			if (valor.charAt(i) != ')') {
				if (i == 0) {
					valor = ')' + valor;
				} else if (i == valor.length) {
					valor = valor + ')';
				} else {
					valor = valor.substring(0, i) + ')' + valor.substring(i);
				}
				indiceFechaPar++;
			}
		}

		if (i == posVirgula[indiceVirgula]) {
			if (valor.charAt(i) != ',') {
				if (i == 0) {
					valor = ',' + valor;
				} else if (i == valor.length) {
					valor = valor + ',';
				} else {
					valor = valor.substring(0, i) + ',' + valor.substring(i);
				}
				indiceVirgula++;
			}
		}
	}

	if (campo.value.length > tamanho) {
		campo.value = campo.value.substring(0, tamanho);
	}

	campo.value = valor;

	// Se for somente números
	if (conteudo == 1) {
		// Retira os espaços em branco
		campo.value = campo.value.trim();
	}
}

function numericMask(campo,decimalHouses,maxlength,showSeparators, event, permitirNegativo) {
	value = campo.value;

	var possuiSinalNegativo = (value.indexOf("-") != -1);
	var possuiSinalPositivo = (value.indexOf("+") != -1);
	is_dirty = true; 
	
	//Por default não permite negativo.
	if(permitirNegativo == null)
	{
	  permitirNegativo = false;
	}
	
	if(value == '') {
		return;
	}

	if(decimalHouses != 0) {
		 showSeparators = true;
	}

    //Retorna para permitir que o cursor seja movimentado no campo através das teclas
    //direcionais
    if ((event.keyCode>=35 && event.keyCode<=40)) {
       return true;
    }
    
    //Preenche zero quando somente o sinal de negativo for digitado e o campo estiver 
    //vazio.
    if(value != null && value.length == 1 && possuiSinalNegativo && permitirNegativo)
    {
      value = "0";
    }
              
	vr = value;
    vr = vr.replace(",", ""); 
    vr = vr.replace(".", ""); 
    vr = vr.replace(".", ""); 
    vr = vr.replace(".", ""); 
    vr = vr.replace(".", ""); 
    vr = vr.replace(".", ""); 
    vr = vr.replace(".", ""); 
    vr = vr.replace(".", ""); 
    vr = vr.replace(".", ""); 
    vr = vr.replace(".", ""); 
    vr = vr.replace(".", ""); 
    vr = vr.replace(".", ""); 
    vr = vr.replace(".", ""); 
	if(vr.length == maxlength && showSeparators) {
		campo.value = '';
		alert("Valor inválido! Ultrapassa o valor máximo!");
		return;
	}
	value = extractNumbers(value);
	if(showSeparators) {
		// Tira os 0s a esquerda
		var temp = value;
		for(i = 0 ; i < value.length-1 ; i++) {
		   if (value.charAt(i) == 0) {
		   		temp = temp.substr(1);
		   }
		   else {
		   		break;
		   }
		}		
		value = temp;
	}	
	
	// Quebra dos valores em inteiro e decimal respeitando o numero de casas decimais
	if ((value.length) < decimalHouses) {
		var integer = value.substring(0, (value.length) - decimalHouses);
        var integer = 0;
	    var dec = value.substring((value.length) - decimalHouses, value.length);	
	    if (event.keyCode==8) {	    	
	        if (decimalHouses!=0) {
				var integer = value.substring(0, (value.length) - decimalHouses);
				var dec = value.substring((value.length) - decimalHouses, value.length);
				if(dec == 0) {
					dec = '';
				}      				
			}
			else {
			    var integer=value.substring(0, value.length);
			    var dec='';
			}		    
	    }
	    if (event.keyCode==8) {  		    		
	    		if(value != '' && dec != '') {
	    			while((dec.length) < decimalHouses)	{
					    dec = '0' + dec;
					}
	    		}
	    		else {
	    			dec = '';
	    		}
	    	}
		if (event.keyCode!=9 && event.keyCode!=8) {
			while((dec.length) < decimalHouses) {
			    dec = '0' + dec;
			}						
	    }
    }
    else if (decimalHouses != 0) {
    	var integer = value.substring(0, (value.length) - decimalHouses);
		var dec = value.substring((value.length) - decimalHouses, value.length);
    }
    else {
		var integer = value.substring(0, (value.length+1) - decimalHouses);
		var dec = value.substring((value.length+1) - decimalHouses, value.length);
    }	
    if (showSeparators) {	
	   temp = '';
	   for (i = integer.length ; i >= 0 ; i = i - 3)
	       temp = integer.substring(i - 3 , i) + '.' + temp;
	   // Tirando os pontos desnecessários
	   if (temp.substring(0, 1) == '.') {
	   		temp = temp.substr(1);
	   }
	   temp = temp.substring(0, temp.length-1);	
	   integer = temp;
    }
    if (decimalHouses!=0 && integer=='' && dec!='') {
       integer='0';
    }    
    if(value != '') {   	
		if(decimalHouses == 0) {
			campo.value = integer;		
			if(possuiSinalNegativo && !possuiSinalPositivo && permitirNegativo)
			{
			  campo.value = "-" + campo.value;
			}				
		}
		else { 		
			if(dec != '') {
				campo.value = integer + ',' + dec;
				if(possuiSinalNegativo && !possuiSinalPositivo && permitirNegativo)
				{
				  campo.value = "-" + campo.value;
				}
			}
			else {
				campo.value = '';
			}
		}
	}
	else {
		campo.value = '';
	}
}

function extractNumbers(s) {
	var r = '';
	
	for(i = 0 ; i < s.length ; i++) {
	   if (!isNaN(s.charAt(i)) && s.charAt(i) != ' '){
	       r = r + s.charAt(i);
	   }
    }
    return r;
}

//funcao que verifica se o valor a ser digitado eh numerico
function isNumericKey(event, permitirNegativo) {
    if (!event) event = window.event;
    if(permitirNegativo == null) {
		permitirNegativo = false;
    }
    key = event.keyCode;
    if (navigator.appName == "Microsoft Internet Explorer") {
	    if (key==0) key = event.which;
		if (event.shiftKey) {
        	if (key==9 || key==35 || key==36) {
            	return true;
	        } else  return false;
	    } else {
        	if (!event.ctrlKey && !event.altKey) {
	   	    	if ((key>47 && key<58) || (key>95 && key<106) || (key>=35 && key<=40) || key==9 || key==8 || key==46) {
              		return true;
	            } else if (permitirNegativo && (key==109 || key==107)) {
		            return true;
        		} else return false;
	        } else return false;
      	}
    } else {
    	if (key==0) {
	        if(event.which != 46) {
  		 		key = event.which;
            }
        }
        if (event.shiftKey) {
	        if (key==9 || key==35 || key==36) {
            	return true;
	        } else {
 	        	event.returnValue = false;
	            return false;
            }
		} else {
	    	if (!event.ctrlKey && !event.altKey) {
 	        	if ((key>47 && key<58) || (key>95 && key<106) || (key>=35 && key<=40) || key==9 || key==8 || key==46) {
	                return true;
    		    } else if (permitirNegativo && (key==109 || key==107)) {
		            return true;
        		} else {
		            event.returnValue = false;
        		    return false;
		        }
        	} else {
 	           event.returnValue = false;
    	       return false;
	        }
     	}
	}
}
