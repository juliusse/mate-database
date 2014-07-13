define(
		[ 'jquery', 'jquerymeta', 'jquerytable', 'app/models/junky',
				'app/collections/junkies', 'app/views/junkies/row',
				"app/views/newUser" ], function($, meta, table, Junky, Junkies,
				JunkyRowView, newUserView) {
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