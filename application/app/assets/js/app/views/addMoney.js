define([
// These are path alias that we configured in our bootstrap
'jquery', // lib/jquery/jquery
'underscore', // lib/underscore/underscore
'backbone', 
'app/utils',
'app/models/junky', 
'app/views/junkies/row',
'text!templates/addMoneyForm.html' ], 
function($, _, Backbone,Utils , Junky,JunkyView, html) {

	addMoneyFormView = Backbone.View.extend({
		defaults : {},
		initialize : function(data) {
			this.render();
		},
		addMoney : function(e) {
			var junkies = MateDatabase.junkies;
			var junkyId = $('select#addMoney-user').val();
			var junky = MateDatabase.junkies.find(function(item) {
				return item.get("id") == junkyId;
			});

			var curAmount = junky.get("credit");
			var amount = $('input#addMoney-amount').val() * 100;

			junky.save({
				credit : curAmount + amount
			}, {
				success : function(junky) {
					new JunkyView({
						el : $('#junky-container'),
						junky : junky
					});
					
					$("#junkyTable").trigger('update');
					$("#junkyTable").tablesorter({
						sortList : [ [ 3, 1 ] ],
						headers : {
							3 : {
								sorter : 'digit'
							}
						}
					});
				}
			});
			return Utils.cancelDefaultAction(e);
		},
		events : {
			"submit #addMoney-form" : "addMoney",
		},
		render : function() {
			var junkies = MateDatabase.junkies;
			// Compile the template using underscore
			var data = {junkies: junkies};
			var template = _.template(html, data);
			// Load the compiled HTML into the Backbone "el"
			this.$el.html(template);
		}
	});

	return addMoneyFormView;

});