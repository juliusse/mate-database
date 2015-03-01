define([ 
'jquery', 
'jquerymeta', 
'jquerytable', 
'app/models/junky',
'app/models/meta',
'app/collections/junkies',
'app/views/junkies/row',
'app/views/newUser', 
'app/views/addMoney',
'app/views/addMate'], 
function($, meta, table,
		Junky, Meta, Junkies, JunkyRowView, newUserView, addMoneyView, addMateView) {
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
	
	new addMateView({
		el : $("#addMate-container")
	});
	
	// update available bottles
	MateDatabase.meta = new Meta({id : 1});
	MateDatabase.meta.refresh();
	
	
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