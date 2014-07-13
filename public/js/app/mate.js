define([ 'jquery', 'jquerymeta', 'jquerytable', 'app/models/junky',
		'app/collections/junkies', 'app/views/junkies/row' ], function($, meta,
		table, Junky, Junkies, JunkyRowView) {
	window.MateDatabase = {};
	MateDatabase.junkies = new Junkies(junkiesJson);

	MateDatabase.junkies.each(function(model) {
		new JunkyRowView({
			el : $("#row-" + model.get("id")),
			junky : model
		});
	});

	$("#junkyTable").tablesorter({
		sortList : [ [ 4, 1 ] ]
	});

	return MateDatabase;
});