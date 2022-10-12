Date.prototype.addDays = function(days) {
    var date = new Date(this.valueOf());
    date.setDate(date.getDate() + days);
    return date;
}

print(new Date().toISOString() +' START--');
var collections = db.getCollectionNames();
var created = new Array();
for(var i=1; i <= 4; i++){
	begin = new Date().addDays(i);
	var newName = begin.toISOString().substr(0,10).replace(/-/g,"");
	print (i +" "+ newName +" "+ begin);

	created.push('SITECODE_'+newName);
	created.push('SCRIPTNO_'+newName);
	created.push('KPI_'+newName);

	if(Array.contains(collections, 'SITECODE_'+newName)) {
		continue;
	}
	if(Array.contains(collections, 'SCRIPTNO_'+newName)) {
                continue;
        }
	if(Array.contains(collections, 'KPI_'+newName)) {
			continue;
	}

	db.createCollection('SITECODE_'+newName);
	db.createCollection('SCRIPTNO_'+newName);
	db.createCollection('KPI_'+newName);
	
	db['SITECODE_'+newName].createIndex( {"STATS_DTTM" : 1,"PLTFOM_TP_CODE" : 1,"SITE_CODE" : 1} );
	db['SCRIPTNO_'+newName].createIndex( {"STATS_DTTM" : 1,"PLTFOM_TP_CODE" : 1,"MEDIA_SCRIPT_NO":1} );
	db['KPI_'+newName].createIndex( {"STATS_DTTM" : 1, "KPI_NO":1, "PAR_NO":1});

	print('createCollection DATE '+ newName);
}

var collections = db.getCollectionNames();
var dropCollection = new Array()

for( var i=-20; i>=-50; i--){
	var targetDate = new Date().addDays(i).toISOString().substr(0,10).replace(/-/g,"");
	dropCollection.push('SITECODE_'+targetDate);
	dropCollection.push('SCRIPTNO_'+targetDate);
	dropCollection.push('KPI_'+targetDate);
}

dropCollection.forEach(function(element, index, array){
	if(Array.contains(collections, element)){
		print('drop '+element);
		db[element].drop();
	}
});


print(new Date() + ' END--');

