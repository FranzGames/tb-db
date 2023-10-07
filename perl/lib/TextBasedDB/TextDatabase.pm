package TextBasedDB::TextDatabase;

use File::Path qw(make_path remove_tree);
use TextBasedDB::TextTable;

use strict;
use vars qw( $VERSION @ISA @EXPORT @EXPORT_OK );
require Exporter;
@ISA = qw(Exporter);
$VERSION="1.00";
@EXPORT = qw(
   openDatabase
   createDatabase
   );


sub openDatabase {
   my ($dir) = @_;

   return new TextBasedDB::TextDatabase($dir);
}

sub createDatabase {
   my ($dir) = @_;

   make_path ($dir);

   return openDatabase($dir);
}

sub new {
   my $class = shift;

   my $self = {
      _directory => shift,
   };

   bless $self, $class;

   return $self;
}

sub getDatabaseDirectory
   {
   my ($self) = @_;

   return $self->{_directory};
   }

sub listOfTables
   {
   my ($self) = @_;
   my @tables = ();

   my $result = opendir my $dir, $self->getDatabaseDirectory();

   if (!$result)
      {
      return @tables;
      }
   
   my @files = readdir $dir;
   closedir $dir;

   foreach my $file (@files)
      {
      if (-d $self->getDatabaseDirectory()."/".$file)
         {
         push (@tables, $file);
         }
      }

   return @tables;
   }

sub getTableFile
   {
   my ($self, $file) = @_;

   return $self->getDatabaseDirectory()."/".$file;
   }

sub openTable {
   my ($self, $table) = @_;
   my $file = $self->getTableFile ($table);

   return TextBasedDB::TextTable::openTable ($self->getTableFile ($table));
}

sub createTable {
   my ($self, $table) = @_;

   return TextBasedDB::TextTable::createTable ($self->getTableFile ($table));
}

1;
