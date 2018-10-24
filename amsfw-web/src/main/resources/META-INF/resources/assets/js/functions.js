//function ocultar(ocultar, exibir) {
//	document.getElementById(ocultar).style.display = "none";
//
//	if (exibir === "desmarcar")
//		document.getElementById(ocultar.replace("selecionar", "desmarcar")).style.display = "inline";
//	else
//		document.getElementById(ocultar.replace("desmarcar", "selecionar")).style.display = "inline";
//}
//
//function onChangeCheck(componente) {
//
//	if (document.getElementById(componente).checked) {
//		$("a[rel=itensDataTableSelected]").each(function(index) {
//			addClassNegrito(this);
//			var id = $($(this)).attr('id');
//			ocultar(id, 'desmarcar');
//		});
//	} else {
//		$("a[rel=itensDataTableNotSelected]").each(function(index) {
//			removeClassNegrito(this);
//			var id = $($(this)).attr('id');
//			ocultar(id, 'selecionar');
//		});
//	}
//}



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
	    if (codigo == 8 || codigo == 46
	    		|| codigo == 35 || codigo == 36 
	    		|| codigo == 37 || codigo == 39
	    	     || codigo == 9) {
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

//var habilitaMenu = function() {
//    var pathName = window.location.pathname.substring(window.location.pathname.lastIndexOf('/') + 1);
//    var classe = pathName.replace(".xhtml","");
//
//    if(classe == ''){
//    	classe = 'index';
//    }
//    var classe = "." + classe;
//	var obj = $(classe);
//
//	if(obj != null && obj.get(0) != null && obj.get(0).tagName != undefined && obj.get(0).tagName == 'SPAN'){
//		var parent = obj.parent().parent();
//		parent.addClass("active");
//		$('<span class="selected"></span>').insertAfter(classe);
//	} else {
//		obj.addClass("active");
//		var parent = obj.parent().parent();
//		parent.addClass("active");
//		parent.find('.arrow').addClass("open");
//		$('<span class="selected"></span>').insertBefore('.arrow');
//	}
//};
//
//
//$(document).ready(function() {
//	
//	 $("a[rel=modal]").click( function(ev){		    	
//			
//    	 var id =$("removerModal").attr('id');
//         var alturaTela = $(document).height();
//         var larguraTela = $(window).width();
//      
//         $('#mascara').css({'width':larguraTela,'height':alturaTela});
//         $('#mascara').fadeTo("slow",0.7);
//         var left = ($(window).width() /2) - ( $(id).width() / 2 );
//         var top = ($(window).height() / 2) - ( $(id).height() / 2 );			      
//         $(id).css({'top':top,'left':left});
//         $(id).show();
//    });
//    $("a[rel=modalUnisys]").click( function(){
//        $("#mascara").hide();
//    });
//
//	habilitaMenu();
//});
 

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