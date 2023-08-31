package TextBasedDB::TextQuery::Criteria;

use strict;
use vars qw( $VERSION @ISA @EXPORT @EXPORT_OK );
require Exporter;
@ISA = qw(Exporter);
$VERSION="1.00";
@EXPORT = qw(
   new
   getColumnName
   setColumnName
   getOperand
   setOperand
   getValue
   setValue
   match
   );

sub new {
   my $class = shift;

   my $self = {
      _name => shift,
      _operand => shift,
      _value => shift,
   };

   bless $self, $class;

   return $self;
}

sub getColumnName
   {
   my ($self) = @_;

   return $self->{_name};
   }

sub setColumnName
   {
   my ($self, $name) = @_;

   $self->{_name} = $name;

   return $self->{_name};
   }

sub getOperand
   {
   my ($self) = @_;

   return $self->{_operand};
   }

sub setOperand
   {
   my ($self, $o) = @_;

   $self->{_operand} = $o;

   return $self->{_operand};
   }

sub getValue
   {
   my ($self) = @_;

   return $self->{_value};
   }

sub setValue
   {
   my ($self, $v) = @_;

   $self->{_value} = $v;

   return $self->{_value};
   }

sub match
   {
   my ($self, $value) = @_;
   my $compareResult = $value cmp $self->getValue();

   if ($self->getOperand() eq '==')
      {
      return $compareResult ? 0 : 1;
      }
   elsif ($self->getOperand() eq '!=')
      {
      return $compareResult ? 1 : 0;
      }
   elsif ($self->getOperand() eq '<')
      {
      return $compareResult < 0 ? 1 : 0;
      }
   elsif ($self->getOperand() eq '<=')
      {
      return $compareResult <= 0 ? 1 : 0;
      }
   elsif ($self->getOperand() eq '>')
      {
      return $compareResult > 0 ? 1 : 0;
      }
   elsif ($self->getOperand() eq '>=')
      {
      return $compareResult >= 0 ? 1 : 0;
      }
   elsif ($self->getOperand() eq 'contains')
      {
      return index($value, $self->getValue()) >= 0 ? 1 : 0;
      }
   elsif ($self->getOperand() eq '!contains')
      {
      return index($value, $self->getValue()) < 0 ? 1 : 0;
      }

   return 0;
   }

1;
