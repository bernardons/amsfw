
$(document).ready(function() {
	
	var contexto = window.location.pathname.substring(1)
    contexto = contexto.substring(0, contexto.indexOf('/'))
	
	loadJsCssFile("/" + contexto + "/assets/js/locales/date_pt_BR.js", "js");
	
	// Se tiver algum componente que utiliza a classe money carrega o javascript de mascara de dinheiro 
	if($(".money").length > 0) {
		loadJsCssFile("/" + contexto + "/assets/js/jquery.maskMoney.js", "js");
		$(".money").maskMoney({
			thousands : '.',
			decimal : ','
		});
	}

	// Se tiver algum componente de data que utiliza a classe data carrega o javascript e css de data
	if($(".date-picker").length > 0) {
		
		$('.date-picker').datepicker({
			format: 'dd/mm/yyyy',
			minDate: null,
		    maxDate: null
		});
	}	

	// Se tiver usando algum componente de mes e ano carrega o plugin do JQuery de mascaras.
	if ($(".mesano").length > 0) {
		loadJsCssFile("/" + contexto + "/assets/js/jquery.mask.min.js", "js");
		$('.mesano').mask("99/9999");
	}
	
	// Se tiver algum componente de data que utiliza a classe de intervalo de data carrega o javascript e css de intervalo de data
	if($(".date-range").length > 0) {
		loadJsCssFile("/" + contexto + "/assets/bootstrap-daterangepicker/daterangepicker.css", "css");
		loadJsCssFile("/" + contexto + "/assets/bootstrap-daterangepicker/date.js", "js");
		loadJsCssFile("/" + contexto + "/assets/bootstrap-daterangepicker/daterangepicker.js", "js");
	}	
	
	
	// habilitaMenu();
});



