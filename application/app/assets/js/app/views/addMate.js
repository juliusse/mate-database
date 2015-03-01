define([
// These are path alias that we configured in our bootstrap
'jquery', // lib/jquery/jquery
'underscore', // lib/underscore/underscore
'backbone', // 
'app/utils', //
'text!templates/addMateForm.html'
], function($, _, Backbone, Utils, html) {

	addMateFormView = Backbone.View.extend({
		defaults : {},
		initialize : function(data) {
			this.render();
		},
		addMate : function(e) {
			var count = $('input#addMate-count').val()
			var data = {};
			data.count = count;
			if(count != 0) {
				$.ajax({
					  type: 'POST',
					  url: 'rest/mate/add',
					  data: JSON.stringify(data),
					  contentType: "application/json; charset=utf-8",
					  success: function() {
						  MateDatabase.meta.refresh();
					  }
					});
			}			return Utils.cancelDefaultAction(e);
		},
		events : {
			"submit #addMate-form" : "addMate",
		},
		render : function() {
			var template = _.template(html, {});
			// Load the compiled HTML into the Backbone "el"
			this.$el.html(template);
		}
	});

	return addMateFormView;

});