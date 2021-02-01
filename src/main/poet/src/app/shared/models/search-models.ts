export interface SearchResult {
  type: string;
  id: string;
  name: string;
}

export interface MonarchSearchResult {
  match: string;
  id: string;
}

export interface HpoTerm {
  childrenCount: number;
  id: string;
  name: string;
  ontologyId: string;
  synonym: string;
}

export interface MaxoTerm {
  comment: string;
  definition: string;
  name: string;
  ontologyId: string;
  synonyms: string[];
}

// Interfaces from HPO API Search
export interface HpoMaxoSearchResult {
  synonym: string;
  synonymMatched: boolean;
  term: MaxoTerm
}

export interface MaxoSearchResult extends MaxoTerm{
  synonym: string;
  synonymMatched: boolean;
}

