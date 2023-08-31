package TextBasedDB::TextTableDefinition;

use TextBasedDB::TextColumnDefinition;

use strict;
use vars qw( $VERSION @ISA @EXPORT @EXPORT_OK );
require Exporter;
@ISA = qw(Exporter);
$VERSION="1.00";
@EXPORT = qw(
   new
   createDefinitionObject
   addColumn
   getColumns
   getColumnNo
   getColumnName
   getDefinitionFile
   load
   write
   );

sub new {
   my $class = shift;
   my @cols = ();

   my $self = {
      _file => shift,
      _columns => \@cols,
   };

   bless $self, $class;

   return $self;
}

sub createDefinitionObject
   {
   my ($self) = @_;

   return new TextBasedDB::TextColumnDefinition();
   }

sub getDefinitionFile
   {
   my ($self) = @_;

   return $self->{_file};
   }
 
sub setDefinitionFile
   {
   my ($self, $file) = @_;

   $self->{_file} = $file if defined ($file);

   return $self->{_file};
   }

sub getColumns
   {
   my ($self) = @_;

   return @{$self->{_columns}};
   }

sub setColumns
   {
   my ($self, @cols) = @_;

   $self->{_columns} = \@cols;

   return $self->{_columns};
   }

sub getNumOfColumns
   {
   my ($self) = @_;

   return scalar ($self->getColumns());
   }

sub getColumnDefinition
   {
   my ($self, $colNo) = @_;

   if ($colNo >= 0 && $colNo < $self->getNumOfColumns())
      {
      my @cols = $self->getColumns ();

      return $cols[$colNo];
      }
   }

sub getColumnName
   {
   my ($self, $colNo) = @_;
   my $col = $self->getColumnDefinition ($colNo);

   if (defined $col)
      {
      return $col->getName();
      }
   }

sub getColumnNo
   {
   my ($self, $name) = @_;
   my @cols = $self->getColumns();

   for (my $i = 0; $i < scalar (@cols); $i++)
      {
      if ($cols[$i]->getName eq $name)
         {
         return $i;
         }
      }

   return -1;
   }
  
sub addColumn
   {
   my ($self, $name) = @_;
   my $col = $self->createDefinitionObject();

   $col->setName($name);

   my @cols = @{$self->{_columns}};

   foreach my $c (@cols)
      {
      if ($c->getName() eq $name)
         {
         return $c;
         }
      }

   push (@cols, $col);

   $self->{_columns} = \@cols;

   return $col;
   } 

sub load
   {
   my ($self) = @_;
   my $file;
   my @cols = ();

   if (!open ($file, "<", $self->getDefinitionFile()))
      {
      return;
      }

   while (<$file>)
      {
      s/[\r\n]//g;

      my $col = $self->createDefinitionObject();

      $col->processLine ($_);

      push (@cols, $col);
      }

   $self->setColumns (@cols);

   close ($file);
   }

sub write
   {
   my ($self) = @_;
   my $file;
   my @cols = $self->getColumns();

   if (!open ($file, ">", $self->getDefinitionFile()))
      {
      return;
      }

   foreach my $col (@cols)
      {
      print $file $col->createLine ()."\n";
      }

   close ($file);
   }

1;
