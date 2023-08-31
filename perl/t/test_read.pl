
use TextBasedDB::TextDatabase;

my $db = openDatabase ('./testdb');

my $table = $db->openTable ('test_table');

my @recIds = $table->listOfRecordIds();

foreach my $recId (@recIds)
   {
   print "recId = $recId\n";

   my $rec = $table->getRecord ($recId);

   print " col(1) = ".$rec->getColumnData (1)."\n";

#   $rec->setColumnData (1, "copy of ".$rec->getColumnData(1));
#   $rec->write();
   }

#$rec->setColumnData (0, "1");
#$rec->setColumnData (1, "test\@domain.com");
#$rec->setColumnData (2, "testpw,afga");
#$rec->write();
