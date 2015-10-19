package TextBasedDB::TextUtil;

use strict;
use vars qw( $VERSION @ISA @EXPORT @EXPORT_OK );
require Exporter;
@ISA = qw(Exporter);
$VERSION="1.00";
@EXPORT = qw(
   parseCsv
   encodeCsv
   encodeString
   );

sub parseCsv
   {
   my ($csvLine) = @_;
   my $state = 0;
   my $isQuote = 0;
   my @cols = ();
   my $build = '';

   if ($csvLine eq '')
      {
      return @cols;
      }

   for (my $i = 0; $i < length ($csvLine); $i++)
      {
      my $ch = substr ($csvLine, $i, 1);

      if ($ch eq '"' && length($build) == 0)
         {
         $isQuote = 1;
         }
      elsif ($state == 1)
         {
         if ($ch eq 'r')
            {
            $build .= "\r";
            }
         elsif ($ch eq 'n')
            {
            $build .= "\n";
            }
         elsif ($ch eq 't')
            {
            $build .= "\t";
            }
         else
            {
            $build .= $ch;
            }

         $state = 0;
         }
      elsif ($ch eq '\\')
         {
         $state = 1;
         }
      elsif ($ch eq '"')
         {
         $isQuote = 1;
         }
      elsif ($ch eq ',')
         {
         if ($isQuote)
            {
            $build .= $ch;
            }
         else
            {
            push (@cols, $build);
            $build = '';
            }
         }
      else
         {
         $build .= $ch;
         }
      }

   push (@cols, $build);

   return @cols;
   }

sub encodeCsv
   {
   my @cols = @_;
   my $builder = '';
   my $first = 1;

   foreach my $col (@cols)
      {
      if (!$first)
         {
         $builder .= ',';
         }

      $first = 0;

      $builder .= encodeString($col);
      }

   return $builder;
   }

sub encodeString
   {
   my ($str) = @_;
   my $isQuoted = (index($str,',') >= 0 ? 1 : 0);
   my $builder = '';

   if ($isQuoted)
      {
      $builder = '"';
      }

   for (my $i = 0; $i < length ($str); $i++)
      {
      my $ch = substr ($str, $i, 1);

      if ($ch eq '\r')
         {
         $builder .= "\\r";
         }
      elsif ($ch eq '\n')
         {
         $builder .= "\\n";
         }
      elsif ($ch eq '\t')
         {
         $builder .= "\\t";
         }
      elsif ($ch eq '\\')
         {
         $builder .= "\\\\";
         }
      else
         {
         $builder .= $ch;
         }
      }

   if ($isQuoted)
      {
      $builder .= '"';
      }

   return $builder;
   }
  
1;
 
