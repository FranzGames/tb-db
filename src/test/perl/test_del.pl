
use TextBasedDB::TextDatabase;

my $db = openDatabase ('./testdb');

my $table = $db->openTable ('test_table');

my @recIds = $table->listOfRecordIds();

if (scalar (@recIds) > 0)
   {
   $table->deleteRecord ($recIds[0]);
   }

