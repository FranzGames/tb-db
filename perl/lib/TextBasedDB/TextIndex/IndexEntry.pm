package TextBasedDB::TextIndex::IndexEntry;

use strict;
use vars qw( $VERSION @ISA @EXPORT @EXPORT_OK );
require Exporter;
@ISA = qw(Exporter);
$VERSION="1.00";
@EXPORT = qw(
   );

sub new {
   my $class = shift;

   my $self = {
      _recid => shift,
   };

   bless $self, $class;

   return $self;
}

sub getRecordId
   {
   my ($self) = @_;

   return $self->{_recid};
   }

sub setRecordId
   {
   my ($self, $file) = @_;

   $self->{_recid} = $file;

   return $self->{_recid};
   }

sub setKeyValues
   {
   my ($self, @cols) = @_;

   $self->{_values} = \@cols;
   }

sub getKeyValues
   {
   my ($self) = @_;

   return @{$self->{_values}};
   }

sub processLine
   {
   my ($self, $str) = @_;

   my @cols = TextBaseDB::TextUtil::parseCsv ($str);
   $self->setRecordId (pop (@cols));
   $self->setKeyValues (@cols);
   }

sub getLine
   {
   my ($self) = @_;

   return  TextBasedDB::TextUtil::encodeCsv($self->getKeyValues()).",".$self->getRecordId();
   }
   

1;
