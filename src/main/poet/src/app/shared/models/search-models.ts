export interface BaseTerm {
  id: string;
  name: string;
}

export interface MondoSearchResult extends BaseTerm {
  match: string;
  omim_id: string;
  leaf: boolean;
}

export interface HpoTerm extends BaseTerm{
  descendantCount?: number;
  synonym?: string;
}

export interface MaxoTerm extends BaseTerm{
  comment: string;
  definition: string;
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

export interface AnchorSearchResult {
  ontologyId: string;
  name: string;
}
