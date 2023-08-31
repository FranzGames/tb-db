
use TextBasedDB::TextDatabase;
use TextBasedDB::TextTable;

my $db = createDatabase ('./testdb');

my $table = $db->createTable ('test_table');

$table->addColumn ("customer_id");
$table->addColumn ("email");
$table->addColumn ("passwd");

my @customer_ids = ('1','2','3','4');

foreach my $id (@customer_ids)
   {
   my $rec = $table->createRecord();

   print "recId = ".$rec->getRecordId()."\n";

   $rec->setColumnData (0, $id);
   $rec->setColumnData (1, "test$id\@domain.com");
   $rec->setColumnData (2, "testpw,afga");
   $rec->write();
   }

