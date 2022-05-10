import { HttpMethod } from '@auth0/auth0-angular';

const POET_BASE_URL = 'https://ctpoet01lt.jax.org/api/v1';
const MONARCH_BASE_URL = 'https://api.monarchinitiative.org/api';
const PUBMED_BASE_URL = 'https://eutils.ncbi.nlm.nih.gov/entrez/eutils/';
const HPO_BASE_URL = 'https://hpo.jax.org/api/';
export const environment = {
  production: true,
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
  HPO_API_HPO_SEARCH_URL: HPO_BASE_URL + 'hpo/search',
  HPO_API_MAXO_SEARCH_URL: HPO_BASE_URL + 'maxo/search/',
  HPO_API_HPO_SEARCH_DESCENDANTS_URL: HPO_BASE_URL + 'hpo/search/descendants/',
  HPO_API_TERM_URL: HPO_BASE_URL + 'hpo/term/',
  MONARCH_SEARCH_URL: MONARCH_BASE_URL + '/search/entity/autocomplete/',
  MONARCH_ENTITY_URL: MONARCH_BASE_URL + '/bioentity/',
  PUBMED_SUMMARY_URL: PUBMED_BASE_URL + 'esummary.fcgi',
  AUTH0_ROLE_CLAIM: 'https://poet.jax.org/role',
  AUTH0_ADMIN_ROLE: 'POET_ADMIN',
  HPO_ANNOTATIONS_URL: 'http://purl.obolibrary.org/obo/hp/hpoa/phenotype.hpoa'
};
