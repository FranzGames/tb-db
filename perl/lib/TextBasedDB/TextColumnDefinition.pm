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
      _type => '',
      _isKey => 0,
   };

   bless $self, $class;

   return $self;
}

sub getType
   {
   my ($self) = @_;

   return $self->{_type};
   }
 
sub setType
   {
   my ($self, $type) = @_;

   $self->{_isKey} = 1 if ($type =~ m/\*/);
   $type =~ s/\*//g;

   $self->{_type} = $type if defined ($type);

   return $self->{_type};
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
   my @parts = split (/=/, $line);

   $self->setName ($parts[0]);
   $self->setType ($parts[1]);
   }

sub createLine
   {
   my ($self) = @_;

   return $self->getName();
   }

1;
