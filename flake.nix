{
  inputs.flake-parts.url = "github:hercules-ci/flake-parts";
  inputs.devenv.url = "github:cachix/devenv";
  inputs.devenv.inputs.nixpkgs.follows = "nixpkgs";

  outputs = inputs@{ flake-parts, nixpkgs, ... }:
    flake-parts.lib.mkFlake { inherit inputs; }
    {
      imports = [ inputs.devenv.flakeModule ];
      systems = [ "x86_64-linux" ];
      perSystem = { pkgs, ... }:{
        devenv.shells.default = {
          packages = with pkgs; [ kotlin-language-server nodePackages.typescript-language-server sqls];
          languages.kotlin = {
            enable = true;
          };
          languages.typescript.enable = true;
          languages.javascript = {
            enable = true;
            npm.enable = true;
          };
          services.postgres = {
            enable = true;
            listen_addresses = "localhost";
            initialDatabases = [
              {
                name = "survey";
              }
            ];
          };
        };
      };
    };
}
