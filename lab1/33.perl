@array = ();
$var = 0;
while (<>){
	if (/<.*?a.*?href\s*=\s*"([^"]+)".*?>/) {
		my @matches = ($_ =~ /.*?a.*?href\s*=\s*"([^"]+)".*?>/g);
		foreach (@matches) {
		  $_ =~ s/<.*?a.*?href\s*=\s*"([^"]+)".*?>/\1/;
		}
		foreach (@matches) {
			if (/^[^.]*$/) {
				next;
			}
			if (/^(.*?:\/\/)?(.*?@)?([^:?@#\/]+?\.[^:?@#\/]+)((:|\?|@|#|\/).*)?$/) {
				s/^(.*?:\/\/)?(.*?@)?([^:?@#\/]+?\.[^:?@#\/]+)((:|\?|@|#|\/).*)?$/\3/;
				push(@array, $_);
			}
		}	
	}
}
%hash = map {$_, 1} @array;
@filtered = keys %hash;
@sorted_filtered_array = sort @filtered;
foreach (@sorted_filtered_array) {
  print "$_\n";
}