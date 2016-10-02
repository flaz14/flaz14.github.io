#include <stdio.h>
#include <glib-object.h>
#include <gio/gio.h>

int is_network_volume(GVolume * volume)
{
	char *identifier = g_volume_get_identifier(volume,
						   G_VOLUME_IDENTIFIER_KIND_CLASS);
	int result = g_strcmp0(identifier, "network") == 0;
	g_free(identifier);
	return result;
}

void print_volume(GVolume * volume)
{
	char *name = g_volume_get_name(volume);
	char *identifier = g_volume_get_identifier(volume,
						   G_VOLUME_IDENTIFIER_KIND_UNIX_DEVICE);

	printf("%s [%s]\n", identifier, name);
	g_free(identifier);
	g_free(name);
}

void print_mounted_volume(GVolume * volume)
{
	printf("+ ");
	print_volume(volume);
}

void print_non_mounted_volume(GVolume * volume)
{
	printf("- ");
	print_volume(volume);
}

void print_connected_drives(GList * connected_drives)
{
	GList *l, *ll;
	for (l = connected_drives; l != NULL; l = l->next) {
		GDrive *drive = l->data;
		GList *volumes = g_drive_get_volumes(drive);
		if (volumes != NULL) {
			for (ll = volumes; ll != NULL; ll = ll->next) {
				GVolume *volume = ll->data;

				if (is_network_volume(volume))
					continue;

				GMount *mount = g_volume_get_mount(volume);

				if (mount != NULL) {
					print_mounted_volume(volume);
					g_object_unref(mount);
				} else {
					print_non_mounted_volume(volume);
				}
				g_object_unref(volume);
			}
			g_list_free(volumes);
		}
	}
	g_list_free(connected_drives);
	return;

}

void print_connected_volumes(GList * connected_volumes)
{
	GList *l;
	for (l = connected_volumes; l != NULL; l = l->next) {
		GVolume *volume = l->data;
		
		// skip physical drives
		GDrive *drive = g_volume_get_drive(volume);
		if (drive != NULL) {
			g_object_unref(volume);
			g_object_unref(drive);
			continue;
		}

		if (is_network_volume(volume))
			continue;

		GMount *mount = g_volume_get_mount(volume);
		if (mount != NULL) {
			print_mounted_volume(volume);
			g_object_unref(mount);
		} else {
			print_non_mounted_volume(volume);
		}
		g_object_unref(volume);
	}
	g_list_free(connected_volumes);
}

int main()
{
	printf("Hi! I'm sample application!\n");
	GVolumeMonitor *volume_monitor = g_volume_monitor_get();

	printf("\nCall g_volume_monitor_get_connected_drives() ...\n");
	GList *connected_drives =
	    g_volume_monitor_get_connected_drives(volume_monitor);
	print_connected_drives(connected_drives);

	printf("\nCall g_volume_monitor_get_volumes() ...\n");
	GList *connected_volumes = g_volume_monitor_get_volumes(volume_monitor);
	print_connected_volumes(connected_volumes);

	printf("\nBye!\n");
	g_object_unref(volume_monitor);
	return 0;
}