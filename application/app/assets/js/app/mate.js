define([ 'jquery', 'jquerymeta', 'jquerytable', 'app/models/junky',
		'app/collections/junkies', 'app/views/junkies/row',
		"app/views/newUser", "app/views/addMoney" ], function($, meta, table,
		Junky, Junkies, JunkyRowView, newUserView, addMoneyView) {
	window.MateDatabase = {};
	MateDatabase.junkies = new Junkies(junkiesJson);

	MateDatabase.junkies.each(function(model) {
		new JunkyRowView({
			el : $("#junky-container"),
			junky : model
		});
	});

	new newUserView({
		el : $("#newUser-container")
	});

	new addMoneyView({
		el : $("#addMoney-container")
	});
	
	// update total count
	var count = MateDatabase.junkies.totalCount();
	$("#totalCount").text(count);

	$("#junkyTable").tablesorter({
		sortList : [ [ 3, 1 ] ],
		headers : {
			3 : {
				sorter : 'digit'
			}
		}
	});

	return MateDatabase;
});