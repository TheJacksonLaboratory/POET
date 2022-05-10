export interface Annotation {
  id?: string;
  type: string;
  status: string;
  annotationSource: AnnotationSource;
  reviewMessages: Message[];
  owner: User;
}

export interface AnnotationSource {
  id?: number;
  publication: Publication;
  disease: Disease;
}

export interface Publication {
  publicationId: string;
  publicationName: string;
  date: string;
  doi: string;
  firstAuthor: string;
  url?: string;
}

export interface Disease {
  diseaseId: string;
  diseaseName: string;
  equivalentId: string;
  description: string;
  phenotypeCount?: string;
  treatmentCount?: string;
}

export interface PhenotypeAnnotation extends Annotation {
  hpoName: string;
  hpoId: string;
  onset: string;
  evidence: string;
  modifier: string;
  qualifier: string;
  frequency: string;
  sex: string;
  description: string;
  lastUpdatedDate: string;
}

export interface TreatmentAnnotation extends Annotation {
  maxoId: string;
  maxoName: string;
  hpoName: string;
  hpoId: string;
  evidence: string;
  comment: string;
  relation: string;
  extension: string;
  lastUpdatedDate: string;
}

export interface UserActivityResponse {
  annotation: any;
  curationAction: string;
  dateTime: string;
  owner: any;
}

export interface HomeIcon {
  imageSrc: string;
  name: string;
  dateAdded: string;
  link: string;
}

export interface Message {
  reviewer: User;
  value: string;
}

export interface User {
  nickname: string;
  userRole: string;
}

export enum Status {
  OFFICIAL = 'OFFICIAL',
  ACCEPTED = 'ACCEPTED',
  UNDER_REVIEW = 'UNDER_REVIEW',
  NEEDS_WORK = 'NEEDS_WORK',
  RETIRED = 'RETIRED'
}

export interface Contribution {
  treatment: number;
  phenotype: number;
}

export enum ReleaseState {
  NOT_STARTED, IN_PROGRESS, DONE_SUCCESS, DONE_ERROR
}
