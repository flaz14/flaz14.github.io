#define _GNU_SOURCE

#include <stdio.h>
#include <stdint.h>
#include <dlfcn.h>
#include <gio/gio.h>

/*
 * This function is a hook for Gnome's function: 
 * (https://developer.gnome.org/gio/stable/GDrive.html#g-drive-get-volumes). 
 * 
 * In contrast to library function this one remains only mounted volumes, but wipes out non-mounted volumes. So Nemo 
 * sees only mounted volumes and knows nothing about volumes which are not mounted yet (or already unmounted).
 */
GList *g_drive_get_volumes(GDrive * drive);

/*
 * Helper functions for pretty printing...
 */
void trace_in();
void trace_out();
void print_drive_identifier(GDrive * drive);

GList *g_drive_get_volumes(GDrive * drive)
{
	trace_in();
	print_drive_identifier(drive);
	static GList *(*original_g_drive_get_volumes) (GDrive *) = NULL;
	// obtain pointer to function we want intercept
	original_g_drive_get_volumes = dlsym(RTLD_NEXT, "g_drive_get_volumes");
	GList *original_volumes = original_g_drive_get_volumes(drive);
	GList *only_mounted_volumes = NULL;
	GList *l;
	if (original_volumes != NULL) {
		for (l = original_volumes; l != NULL; l = l->next) {
			GVolume *volume = l->data;
			GMount *mount = g_volume_get_mount(volume);
			// it's easy to distinguish mounted volume from non-mounted volume:
			// for non-mounted volume g_volume_get_mount() returns NULL.
			if (mount != NULL) {
				only_mounted_volumes =
				    g_list_append(only_mounted_volumes, volume);
				g_object_unref(mount);
			} else {
				g_object_unref(volume);
			}
		}
	}
	trace_out();
	return only_mounted_volumes;
}

void trace_in()
{
	printf(">\n");
}

void trace_out()
{
	printf("<\n");
}

void print_drive_identifier(GDrive * drive)
{
	char *identifier =
	    g_drive_get_identifier(drive, G_VOLUME_IDENTIFIER_KIND_UNIX_DEVICE);
	printf("    %s\n", identifier);
	g_free(identifier);
}
