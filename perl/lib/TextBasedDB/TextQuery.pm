package TextBasedDB::TextQuery;

use strict;
use vars qw( $VERSION @ISA @EXPORT @EXPORT_OK );
require Exporter;
@ISA = qw(Exporter);
$VERSION="1.00";
@EXPORT = qw(
   );

sub new {
   my $class = shift;
   my @crit = ();

   my $self = {
     _matchAll => 0,
     _criteria => \@crit
   };

   bless $self, $class;

   return $self;
}

sub setCriteria
   {
   my ($self, @crit) = @_;

   $self->{_criteria} = \@crit;
   }

sub getCriteria
   {
   my ($self) = @_;

   return @{$self->{_criteria}};
   }

sub isMatchAll
   {
   my ($self) = @_;

   return $self->{_matchAll};
   }

sub setMatchAll
   {
   my ($self, $m) = @_;

   $self->{_matchAll} = $b;

   return $self->{_matchAll};
   }

sub addCriteria
   {
   my ($self, $crit) = @_;
   my @crits = $self->getCriteria();

   push (@crits, $crit);

   $self->setCriteria(@crits);
   }

sub match
   {
   my ($self, $table, $rec) = @_;
   my $result = $self->isMatchAll();
   my @crits = $self->getCriteria();

   foreach my $crit (@crits)
      {
      my $colNo = $table->getColumnNo($crit->getColumnName());
      my $value = $rec->getColumnData($colNo);
      my $critResult = $crit->match($value);

      if (!$critResult && $self->isMatchAll())
         {
         $result = 0;
         last;
         }

      if ($critResult && !$self->isMatchAll())
         {
         $result = 1;
         last;
         }
      }

   return $result;
   }

1;
