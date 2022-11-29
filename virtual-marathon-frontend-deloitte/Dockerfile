FROM node:lts-alpine as build-stage

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

##RUN ng serve --o



# production stage

FROM nginx:stable as production-stage

COPY --from=build-stage /app/dist/* /var/www

RUN rm /etc/nginx/conf.d/default.conf

COPY ./nginx.conf /etc/nginx/conf.d/default.conf
