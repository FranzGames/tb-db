package TextBasedDB::TextTable;

use File::Path qw(make_path remove_tree);
use TextBasedDB::TextTableDefinition;
use TextBasedDB::TextRecord;
use TextBasedDB::TextIndex;

use strict;
use vars qw( $VERSION @ISA @EXPORT @EXPORT_OK );
require Exporter;
@ISA = qw(Exporter);
$VERSION="1.00";
@EXPORT = qw(
   new
   openTable
   createTable
   deleteTable
   getRecordsDirectory
   getIndexesDirectory
   getDefinitionFile
   getRecordFile
   setupTable
   createTableDefinitionObject
   loadDefinition
   writeDefinition
   setTableDefinition
   getTableDefinition
   getColumnNo
   getColumnName
   findRecord
   findRecords
   listOfRecordIds
   getIndex
   createIndex
   );

sub openTable {
   my ($dir) = @_;

   my $table = new TextBasedDB::TextTable($dir);

   $table->loadDefinition();

   return $table; 
}

sub createTable {
   my ($dir) = @_;

   make_path ($dir);

   my $table = openTable($dir);

   $table->setupTable();

   return $table;
}

sub new {
   my $class = shift;

   my $self = {
      _directory => shift,
   };

   bless $self, $class;

   return $self;
}

sub getTableDirectory
   {
   my ($self) = @_;

   return $self->{_directory};
   }

sub isValid
   {
   my ($self) = @_;

   if (!-e $self->getRecordDirectory() || !-d $self->getRecordsDirectory())
      {
      return 0;
      }

   if (!-e $self->getIndexesDirectory() || !-d $self->getIndexesDirectory())
      {
      return 0;
      } 

   return 1;
   }

sub deleteTable
   {
   my ($self) = @_;

   remove_tree ($self->getTableDirectory(), {error  => \my $err_list,});
   }

sub getRecordsDirectory
   {
   my ($self) = @_;
 
   return $self->getTableDirectory()."/records"; 
   } 

sub getIndexesDirectory
   {
   my ($self) = @_;
 
   return $self->getTableDirectory()."/indexes"; 
   } 

sub getDefinitionFile
   {
   my ($self) = @_;
 
   return $self->getTableDirectory()."/columns.txt"; 
   } 

sub getRecordFile
   {
   my ($self, $recId) = @_;

   return $self->getRecordsDirectory()."/$recId.csv";
   }

sub generateRecordId
   {
   my ($self) = @_;

   my $str = time."-".rand();

   return $str;
   }

sub generateRecordFile
   {
   my ($self) = @_;
   my $recId = $self->generateRecordId();
   my $file = $self->getRecordFile ($recId);
   my $num = 1;

   while (-e $file)
     {
     $file = $self->getRecordFile ("$recId-$num");

     $num++;
     }
 
   my $newFile;
 
   open ($newFile, ">", $file);
   close $newFile; 

   return $file;
   }

sub setupTable {
   my ($self) = @_;

   make_path ($self->getRecordsDirectory());
   make_path ($self->getIndexesDirectory());

   setTableDefinition ($self->createTableDefinitionObject());

   $self->writeDefinition(); 
}

sub createTableDefinitionObject {
   my ($self, $file) = @_;

   return new TextBasedDB::TextTableDefinition ($file);
}

sub setTableDefinition
   {
   my ($self, $def) = @_;

   $self->{_definition} = $def if defined ($def);

   return $self->{_definition};
   }

sub getTableDefinition
   {
   my ($self) = @_;

   return $self->{_definition};
   }

sub loadDefinition
   {
   my ($self) = @_;

   my $def = $self->createTableDefinitionObject ($self->getDefinitionFile());

   $def->load();

   $self->setTableDefinition ($def);
   }

sub writeDefinition
   {
   my ($self) = @_;

   $self->getTableDefinition()->write();
   }

sub addColumn
   {
   my ($self, $colName) = @_;

   if ($colName eq '')
      {
      return 0;
      }

   if ($colName =~ m/-/)
      {
      return 0;
      }

   $self->getTableDefinition()->addColumn ($colName);
   $self->writeDefinition();

   return 1;
   }

sub getColumnNo
   {
   my ($self, $name) = @_;

   return $self->getTableDefinition()->getColumnNo($name);
   }

sub getColumnName
   {
   my ($self, $colNo) = @_;

   return $self->getTableDefinition()->getColumnName($colNo);
   }

sub getNumOfColumns
   {
   my ($self) = @_;

   return $self->getTableDefinition()->getNumOfColumns();
   }

sub createRecordObject
   {
   my ($self, $file) = @_;

   return new TextBasedDB::TextRecord ($file);
   }

sub createRecord
   {
   my ($self) = @_;
   my $file = $self->generateRecordFile();
   my $rec = $self->createRecordObject ($file);

   $rec->fillRecord($self->getNumOfColumns());
   $rec->write();

   return $rec;
   }

sub getRecord
   {
   my ($self, $recId) = @_;
   my $file = $self->getRecordFile($recId);
   my $rec = $self->createRecordObject ($file);

   $rec->read();
   $rec->fillRecord($self->getNumOfColumns());

   return $rec;
   }

sub deleteRecord
   {
   my ($self, $recId) = @_;
   my $file = $self->getRecordFile($recId);
   my $rec = $self->createRecordObject ($file);

   $rec->delete();

   return $rec;
   }

sub findRecord
   {
   my ($self, $query, $index) = @_;
   my @recIds = ();

   if (defined $index)
      {
      @recIds = $index->getRecordIds();
      }
   else
      {
      @recIds = $self->listOfRecordIds();
      }

   foreach my $recId (@recIds)
      {
      my $rec = $self->getRecord ($recId);


      if (!defined $query)
         {
         return $rec;
         }
      else
         {
         if ($query->match ($self, $rec))
            {
            return $rec;
            }
         }
      }

   return undef;
   }

sub findRecords
   {
   my ($self, $query, $index) = @_;
   my @recIds = ();
   my @recs = ();

   if (defined $index)
      {
      @recIds = $index->getRecordIds();
      }
   else
      {
      @recIds = $self->listOfRecordIds();
      }

   foreach my $recId (@recIds)
      {
      my $rec = $self->getRecord ($recId);

      if (!defined $query)
         {
         push (@recs, $rec);
         }
      else
         {
         if ($query->match ($self, $rec))
            {
            push (@recs, $rec);
            }
         }
      }

   return @recs;
   }

sub listOfRecordIds
   {
   my ($self) = @_;
   my @files = ();

   my $result = opendir my $dir, $self->getRecordsDirectory();

   if (!$result)
      {
      return @files;
      }
   
   my @dirFiles = readdir $dir;
   closedir $dir;

   foreach my $file (@dirFiles)
      {
      if ((-f $self->getRecordsDirectory()."/".$file) && ($file =~ m/\.csv$/))
         {
         push (@files, TextBasedDB::TextRecord::getRecordIdFromFile($file));
         }
      }

   return @files;
   }  

sub createIndex
   {
   my ($self, @columns) = @_;
   my $file = $self->getIndexesDirectory()."/".getIndexFilename(@columns);
   my $index = new TextBasedDB::TextIndex ($file);
   my @recIds = $self->listOfRecordIds();

   foreach my $recId (@recIds)
      {
      my $rec = $self->getRecord ($recId);

      $index->addEntry ($rec, $self);
      }

   $index->write();

   return $index;
   }

sub getIndex
   {
   my ($self, @columns) = @_;
   my $file = $self->getIndexesDirectory()."/".getIndexFilename(@columns);

   my $index = new TextBasedDB::TextIndex ($file);

   $index->read();

   return $index;
   }
   
1;
