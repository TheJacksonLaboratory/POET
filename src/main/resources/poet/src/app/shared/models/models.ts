export interface SearchResult {
  type: string;
  id: string;
  name: string;
}

export interface MonarchSearchResult {
  match: string;
  id: string;
}

export interface Annotation {
  type: string;
  status: string;
  annotationSource: AnnotationSource;
}

export interface AnnotationSource {
  id: number;
  publication: Publication;
  disease: Disease;
}

export interface Publication {
  id: string;
  name: string;
  date: string;
  doi: string;
  firstAuthor: string;
}

export interface Disease {
  diseaseId: string;
  diseaseName: string;
}

export interface MaxoAnnotation extends Annotation {
  maxoId: string;
  maxoName: string;
  hpoName: string;
  hpoId: string;
  eco: string;
  comment: string;
  relation: string;
  extension: string;
}

export interface HomeIcon {
  imageSrc: string;
  name: string;
  dateAdded: string;
  link: string;
}
