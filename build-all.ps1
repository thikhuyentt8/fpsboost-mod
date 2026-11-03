$ErrorActionPreference = 'Stop'

$fabricApiVersions = [ordered]@{
	'1.21.1' = '0.116.17+1.21.1'
	'1.21.2' = '0.106.1+1.21.2'
	'1.21.3' = '0.114.1+1.21.3'
	'1.21.4' = '0.119.4+1.21.4'
	'1.21.5' = '0.128.2+1.21.5'
	'1.21.6' = '0.128.2+1.21.6'
	'1.21.7' = '0.129.0+1.21.7'
	'1.21.8' = '0.136.1+1.21.8'
	'1.21.9' = '0.134.1+1.21.9'
	'1.21.10' = '0.138.4+1.21.10'
	'1.21.11' = '0.141.6+1.21.11'
}

$yarnMappings = [ordered]@{
	'1.21.1' = '1.21.1+build.3'
	'1.21.2' = '1.21.2+build.1'
	'1.21.3' = '1.21.3+build.2'
	'1.21.4' = '1.21.4+build.8'
	'1.21.5' = '1.21.5+build.1'
	'1.21.6' = '1.21.6+build.1'
	'1.21.7' = '1.21.7+build.8'
	'1.21.8' = '1.21.8+build.1'
	'1.21.9' = '1.21.9+build.1'
	'1.21.10' = '1.21.10+build.3'
	'1.21.11' = '1.21.11+build.6'
}

foreach ($minecraftVersion in $fabricApiVersions.Keys) {
	Write-Host "Building Minecraft $minecraftVersion..."
	& .\gradlew.bat build "-Pminecraft_version=$minecraftVersion" "-Pyarn_mappings=$($yarnMappings[$minecraftVersion])" "-Pfabric_api_version=$($fabricApiVersions[$minecraftVersion])"
	if ($LASTEXITCODE -ne 0) {
		throw "Build failed for Minecraft $minecraftVersion."
	}
}