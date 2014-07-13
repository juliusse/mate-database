define([ 'jquery', 'app/models/junky', 'app/collections/junkies',
		'app/views/junkies/row' ], function($, Junky, Junkies, JunkyRowView) {
	var junkies = new Junkies(junkiesJson);

	junkies.each(function(model) {
		new JunkyRowView({
			el : $("#row-" + model.get("id")),
			junky : model
		});
	});

});