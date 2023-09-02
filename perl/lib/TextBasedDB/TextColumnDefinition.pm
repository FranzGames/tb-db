package TextBasedDB::TextColumnDefinition;

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
      _name => shift,
   };

   bless $self, $class;

   return $self;
}

sub getName
   {
   my ($self) = @_;

   return $self->{_name};
   }
 
sub setName
   {
   my ($self, $name) = @_;

   $self->{_name} = $name if defined ($name);

   return $self->{_name};
   }

sub processLine
   {
   my ($self, $line) = @_;

   $self->setName ($line);
   }

sub createLine
   {
   my ($self) = @_;

   return $self->getName();
   }

1;
