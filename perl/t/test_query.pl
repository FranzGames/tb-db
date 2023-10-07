
use TextBasedDB::TextDatabase;
use TextBasedDB::TextQuery;
use TextBasedDB::TextQuery::Criteria;

my $db = openDatabase ('./testdb');

my $table = $db->openTable ('test_table');

my $query = new TextBasedDB::TextQuery();
my $crit = new TextBasedDB::TextQuery::Criteria ('customer_id', '==', '1');
$query->addCriteria ($crit);

my $rec = $table->findRecord ($query);

if (!defined $rec)
   {
   print "Record not found!!\n";
   }
else
   {
   print "Record id = ".$rec->getRecordId()."\n";

   print "Column 'email' : ".$rec->getColumn ("email")."\n";

   $rec->setColumnData (1, "test@test.com");
   $rec->write()
   }

