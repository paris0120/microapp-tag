import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Server e2e test', () => {
  const serverPageUrl = '/tag/server';
  const serverPageUrlPattern = new RegExp('/tag/server(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const serverSample = { server: 'Response Hawaii' };

  let server;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/tag/api/servers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/tag/api/servers').as('postEntityRequest');
    cy.intercept('DELETE', '/services/tag/api/servers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (server) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/tag/api/servers/${server.id}`,
      }).then(() => {
        server = undefined;
      });
    }
  });

  it('Servers menu should load Servers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tag/server');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Server').should('exist');
    cy.url().should('match', serverPageUrlPattern);
  });

  describe('Server page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(serverPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Server page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tag/server/new$'));
        cy.getEntityCreateUpdateHeading('Server');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serverPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/tag/api/servers',
          body: serverSample,
        }).then(({ body }) => {
          server = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/tag/api/servers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/tag/api/servers?page=0&size=20>; rel="last",<http://localhost/services/tag/api/servers?page=0&size=20>; rel="first"',
              },
              body: [server],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(serverPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Server page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('server');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serverPageUrlPattern);
      });

      it('edit button click should load edit Server page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Server');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serverPageUrlPattern);
      });

      it('edit button click should load edit Server page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Server');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serverPageUrlPattern);
      });

      it('last delete button click should delete instance of Server', () => {
        cy.intercept('GET', '/services/tag/api/servers/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('server').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serverPageUrlPattern);

        server = undefined;
      });
    });
  });

  describe('new Server page', () => {
    beforeEach(() => {
      cy.visit(`${serverPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Server');
    });

    it('should create an instance of Server', () => {
      cy.get(`[data-cy="server"]`).type('Lanka CSS').should('have.value', 'Lanka CSS');

      cy.get(`[data-cy="uuid"]`)
        .type('d531428a-26fa-4fe6-889b-b849f3de3541')
        .invoke('val')
        .should('match', new RegExp('d531428a-26fa-4fe6-889b-b849f3de3541'));

      cy.get(`[data-cy="decoder"]`).type('Investor Minnesota').should('have.value', 'Investor Minnesota');

      cy.get(`[data-cy="password"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        server = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', serverPageUrlPattern);
    });
  });
});
