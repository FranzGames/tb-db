package TextBasedDB::TextIndex;

use TextBasedDB::TextIndex::IndexEntry;

use File::Basename;

use strict;
use vars qw( $VERSION @ISA @EXPORT @EXPORT_OK );
require Exporter;
@ISA = qw(Exporter);
$VERSION="1.00";
@EXPORT = qw(
   getIndexFilename
   getIndexFile
   setIndexFile
   new
   addEntry
   deleteRecordId
   getRecordIds
   read
   write
   sortEntries
   );

sub getIndexFilename
   {
   my @cols = @_;
   my $builder = '';
   my $first = 1;

   foreach my $col (@cols)
      {
      if (!$first)
         {
         $builder .= '-';
         }

      $first = 0;

      $builder .= $col;
      }

    return $builder;
    }

sub new {
   my $class = shift;
   my @entries = ();
   my @cols = ();

   my $self = {
      _file => shift,
      _columns => \@cols,
      _entries => \@entries
   };

   bless $self, $class;

   $self->extractColumnNames();

   return $self;
}

sub getIndexFile
   {
   my ($self) = @_;

   return $self->{_file};
   }

sub setIndexFile
   {
   my ($self, $file) = @_;

   $self->{_file} = $file;

   return $self->{_file};
   }

sub setColumns
   {
   my ($self, @cols) = @_;

   $self->{_columns} = \@cols;
   }

sub getColumns
   {
   my ($self) = @_;

   return @{$self->{_columns}};
   }

sub extractColumnNames
   {
   my ($self) = @_;
   my $filename = basename ($self->getIndexFile(), ".txt");
   my @parts = split (/-/, $filename);

   $self->setColumns (@parts);
   }

sub addEntry
   {
   my ($self, $rec, $table) = @_;
   my @values = ();

   if ((!defined $rec) || (!defined $table))
      {
      return;
      }

   my @cols = $self->getColumns();

   foreach my $col (@cols)
      {
      my $index = $table->getColumnNo ($col);
      my $value = '';

      if ($index >= 0)
         {
         $value = $rec->getColumnData ($index);
         }

      push (@values, $value);
      }

   my $entry = new TextBasedDB::TextIndex::IndexEntry ($rec->getRecordId());

   $entry->setKeyValues (@values);

   my @entries = @{$self->{_entries}};

   push (@entries, $entry);

   $self->{_entries} = \@entries;
   }

sub sortEntries
   {
   my ($self) = @_;
   my @entries = @{$self->{_entries}};
   my @newEntries = sort entrySort @entries; 

   $self->{_entries} = \@newEntries;
   }

sub entrySort
   {
   my @aKeyValue = $a->getKeyValues();
   my @bKeyValue = $b->getKeyValues();

   for (my $i = 0; $i < scalar (@aKeyValue); $i++)
      {
      my $compare = $aKeyValue[$i] cmp $bKeyValue[$i];

      if ($compare)
         {
         return $compare;
         }
      }

   return 0;
   }

sub read
   {
   my ($self) = @_;
   my $fh;
   my @entries = ();

   if (!open ($fh, "<", $self->getIndexFile()))
      {
      return;
      }

   while (<$fh>)
      {
      s/[\r\n]//g;

      my $entry = new TextBasedDB::TextIndex::IndexEntry();
      $entry->processLine();

      push (@entries, $entry);
      }

   close ($fh);

   $self->{_entries} = \@entries;
   }
   
sub write 
   {
   my ($self) = @_;
   my $fh;
   my @entries = @{$self->{_entries}};

   if (!open ($fh, ">", $self->getIndexFile()))
      {
      return;
      }

   foreach my $entry (@entries)
      {
      print $fh $entry->getLine()."\n";
      }

   close ($fh);
   }

sub getRecordIds
   {
   my ($self) = @_;
   my @entries = @{$self->{_entries}};
   my @recIds = ();

   foreach my $entry (@entries)
      {
      push (@recIds, $entry->getRecordId());
      }

   return @recIds;
   }

sub deleteRecordId
   {
   my ($self, $recId) = @_;
   my @entries = @{$self->{_entries}};

   for (my $i = 0; $i < scalar (@entries);$i++)
      {
      my $entry = $entries[$i];

      if ($entry->getRecordId() eq $recId)
         {
         splice (@entries, $i, 1);

         $self->{_entries} = \@entries;
         }
      }
   }

   
1;
