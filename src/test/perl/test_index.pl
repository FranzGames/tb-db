
use TextBasedDB::TextDatabase;

my $db = openDatabase ('./testdb');

my $table = $db->openTable ('test_table');

$table->createIndex ("customer_id");
