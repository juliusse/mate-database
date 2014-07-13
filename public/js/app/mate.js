define([ 'jquery', 'app/models/junky', 'app/collections/junkies',
		'app/views/junkies/row' ], function($, Junky, Junkies, JunkyRowView) {
	window.MateDatabase = {};
	MateDatabase.junkies = new Junkies(junkiesJson);

	MateDatabase.junkies.each(function(model) {
		new JunkyRowView({
			el : $("#row-" + model.get("id")),
			junky : model
		});
	});

	return MateDatabase;
});