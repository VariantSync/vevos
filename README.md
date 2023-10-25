# VEVOS 2.0 (Variant Evolution Simulation)

## Description
This project is a redesign and fresh implementation of our VEVOS framework that combines [VEVOS_Extraction][VEVOS_Extraction] and [VEVOS_Simulation][VEVOS_Simulation] in a single library.
VEVOS has been completely redesigned and is now based on [DiffDetective][DiffDetective] which can be used to implement various analyses of the edit histories of software product lines.  
VEVOS uses DiffDetective to extract the feature mappings, presence conditions, and code matching of an SPL. 

## Planned Features
### CLI
- Extract a ground truth for all commits of an SPL
- Extract a ground truth for a list of SPLs

### Ground Truth Extraction
- Extract the feature mappings and presence conditions for a specific commit of an SPL
- Extract the code matching for a specific commit of an SPL
- Extract the set of features for a specific commit of an SPL
- Extraction of all ground truth data for a list of SPLs (i.e., DiffDetective analysis)
- Serialization of extracted data
- Allow filtering of files when extracting data
  - Changed-only filter
  - Generic path-based filter

### Variant Simulation
- Sample variant configurations from the set of features
- Generate the source code of a variant for a specific commit
- Derive the feature mappings and presence conditions for a variant
- Derive the code matching for a variant
- Deserialization of ground truth data
- Allow filtering of files when generating variants
  - Changed-only filter
  - Generic path-based filter

## Limitations
Due to the redesign of VEVOS, it is no longer capable of extracting feature models, which is a complex and error-prone task. 
The feature model extraction capabilities can still be found in [VEVOS_Extraction][VEVOS_Extraction].



[VEVOS_Extraction]: https://github.com/VariantSync/VEVOS_Extraction
[VEVOS_Simulation]: https://github.com/VariantSync/VEVOS_Simulation
[DiffDetective]: https://github.com/VariantSync/DiffDetective