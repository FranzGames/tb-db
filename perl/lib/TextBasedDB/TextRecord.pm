package TextBasedDB::TextRecord;

use File::Basename;
use TextBasedDB::TextUtil;

use strict;
use vars qw( $VERSION @ISA @EXPORT @EXPORT_OK );
require Exporter;
@ISA = qw(Exporter);
$VERSION="1.00";
@EXPORT = qw(
   getRecordIdFromFile
   new
   getRecordFile
   getRecordId
   read
   write
   setColumnData
   getColumnData
   setData
   getData
   );

sub getRecordIdFromFile
   {
   my ($file) = @_;

   return basename($file,  ".csv");
   }

sub new {
   my $class = shift;
   my @data = ();

   my $self = {
      _file => shift,
      _data => \@data
   };

   bless $self, $class;

   return $self;
}

sub getRecordFile
   {
   my ($self) = @_;

   return $self->{_file};
   }
 
sub setRecordFile
   {
   my ($self, $file) = @_;

   $self->{_file} = $file if defined ($file);

   return $self->{_file};
   }

sub delete
   {
   my ($self) = @_;

   unlink ($self->getRecordFile());
   }

sub getRecordId
   {
   my ($self) = @_;

   return getRecordIdFromFile($self->getRecordFile());
   }

sub fillRecord
   {
   my ($self, $num) = @_;

   my @cols = $self->getData();

   if (!@cols)
      {
      @cols = ();
      }

   while (scalar (@cols) < $num)
      {
      push (@cols, '');
      }

   $self->setData (@cols);
   }

sub read
   {
   my ($self) = @_;
   my $fh;
   my $line = '';

   if (!open ($fh, "<", $self->getRecordFile()))
      {
      return;
      }

   while (<$fh>)
      {
      s/[\r\n]//g;

      $line = $_;
      }

   close ($fh);
   my @cols = TextBasedDB::TextUtil::parseCsv($line);

   $self->setData (@cols);
   }

sub write
   {
   my ($self) = @_;
   my $fh;

   if (!open ($fh, ">", $self->getRecordFile()))
      {
      return;
      }

   print $fh encodeCsv ($self->getData())."\n";

   close ($fh);
   }

sub setColumnData
   {
   my ($self, $colNo, $data) = @_;

   if ($colNo < 0)
      {
      return;
      }

   $self->fillRecord ($colNo + 1);

   ${$self->{_data}}[$colNo] = $data;
   }

sub getColumnData
   {
   my ($self, $colNo, $data) = @_;

   if ($colNo < 0 || $colNo >= scalar ($self->getData()))
      {
      return '';
      }

   return ${$self->{_data}}[$colNo];
   }

sub getData
   {
   my ($self) = @_;

   return @{$self->{_data}};
   }

sub setData
   {
   my ($self, @cols) = @_;

   $self->{_data} = \@cols;

   return $self->{_data};
   }

1;
