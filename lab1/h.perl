while (<>){
    print if /\([^()]*\w[^()]*\)/;
}