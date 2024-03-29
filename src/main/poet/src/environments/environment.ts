// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
import { HttpMethod } from '@auth0/auth0-angular';

const POET_BASE_URL = 'http://localhost:8080/api/v1';
const PUBMED_BASE_URL = 'https://eutils.ncbi.nlm.nih.gov/entrez/eutils/';
const HPO_BASE_URL = 'https://hpo.jax.org/api/';
const ONTOLOGY_SERVICE_BASE_URL = 'https://ontology.jax.org/api/';
export const environment = {
  production: false,
  auth: {
    domain: 'dev-poet.us.auth0.com',
    clientId: 'zAquxh5T1sPsoqBmSd8R7UasuFCcl9LY',
    audience: 'poet-api-audience'
  },
  httpInterceptor: {
    allowedList: [
      POET_BASE_URL + '/statistics/activity/',
      POET_BASE_URL + '/statistics/contributions/',
      POET_BASE_URL + '/statistics/annotation/work',
      {
        uri: POET_BASE_URL + '/annotation/*',
        httpMethod: HttpMethod.Get,
      },
      {
        uri: POET_BASE_URL + '/user/*',
        httpMethod: HttpMethod.Get,
      },
      {
        uri: POET_BASE_URL + '/user/*',
        httpMethod: HttpMethod.Post,
      },
      {
        uri: POET_BASE_URL + '/annotation/*',
        httpMethod: HttpMethod.Post,
      },
      {
        uri: POET_BASE_URL + '/annotation/*',
        httpMethod: HttpMethod.Put,
      },
      {
        uri: POET_BASE_URL + '/annotation/*',
        httpMethod: HttpMethod.Delete,
      },
      {
        uri: POET_BASE_URL + '/entity/disease/',
        httpMethod: HttpMethod.Put,
      },
      {
        uri: POET_BASE_URL + '/entity/publication/',
        httpMethod: HttpMethod.Post,
      },
    ]
  },
  POET_BASE_URL,
  POET_API_USER_URL: POET_BASE_URL + '/user/',
  POET_API_CHECK_USER_URL: POET_BASE_URL + '/user/check',
  POET_API_SEARCH_URL: POET_BASE_URL + '/search',
  POET_API_TREATMENTS_ANNOTATION: POET_BASE_URL + '/annotation/treatments/',
  POET_API_PHENOTYPES_ANNOTATION: POET_BASE_URL + '/annotation/phenotypes/',
  POET_API_DISEASE_ENTITY_URL: POET_BASE_URL + '/entity/disease/',
  POET_API_PUBLICATION_ENTITY_URL: POET_BASE_URL + '/entity/publication/',
  POET_API_STATISTICS_ACTIVITY_URL: POET_BASE_URL + '/statistics/activity/',
  POET_API_STATISTICS_ACTIVITY_DISEASE_URL: POET_BASE_URL + '/statistics/activity/disease',
  POET_API_STATISTICS_CONTRIBUTION_URL: POET_BASE_URL + '/statistics/contributions/',
  POET_API_STATISTICS_ANNOTATION_URL: POET_BASE_URL + '/statistics/annotation/',
  POET_API_EXPORT_URL: POET_BASE_URL + '/export/',
  POET_API_RELEASE_URL: POET_BASE_URL + '/export/release',
  HPO_API_HPO_SEARCH_URL: ONTOLOGY_SERVICE_BASE_URL + 'hp/search',
  HPO_API_MAXO_SEARCH_URL: ONTOLOGY_SERVICE_BASE_URL + 'maxo/search',
  HPO_API_HPO_SEARCH_DESCENDANTS_URL: HPO_BASE_URL + 'hpo/search/descendants/',
  HPO_API_TERM_URL: ONTOLOGY_SERVICE_BASE_URL + 'hp/terms/',
  MONDO_SEARCH_URL: ONTOLOGY_SERVICE_BASE_URL + 'mondo/search',
  MONDO_ENTITY_URL: ONTOLOGY_SERVICE_BASE_URL + 'mondo/terms/',
  PUBMED_SUMMARY_URL: PUBMED_BASE_URL + 'esummary.fcgi',
  AUTH0_ROLE_CLAIM: 'https://poet.jax.org/role',
  AUTH0_ADMIN_ROLE: 'POET_ADMIN',
  HPO_ANNOTATIONS_URL: 'https://github.com/obophenotype/human-phenotype-ontology/releases/latest/download/phenotype.hpoa'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
