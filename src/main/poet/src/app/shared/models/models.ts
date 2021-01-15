export interface Annotation {
  type: string;
  status: string;
  annotationSource: AnnotationSource;
}

export interface AnnotationSource {
  id?: number;
  publication: Publication;
  disease: Disease;
  ontology?: string;
}

export interface Publication {
  publicationId: string;
  publicationName: string;
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
  evidence: string;
  comment: string;
  relation: string;
  extension: string;
}

export interface UserActivityResponse {
  annotation: object;
  curationAction: string;
  localDateTime: string;
  user: object;
}

export interface HomeIcon {
  imageSrc: string;
  name: string;
  dateAdded: string;
  link: string;
}
